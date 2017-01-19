package arpinum.infrastructure.bus;

import arpinum.command.ValidationException;
import arpinum.ddd.BusinessError;
import arpinum.ddd.evenement.Synchrone;
import com.google.common.collect.*;
import io.reactivex.Observable;
import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.*;

import java.util.*;

public abstract class AsynchronousBus implements Bus {

    public AsynchronousBus(Set<? extends SynchronisationBus> synchronisations, Set<? extends MessageCaptor> handlers) {
        for (MessageCaptor handler : handlers) {
            this.handlers.put(handler.commandType(), handler);
        }
        this.synchronisations.addAll(synchronisations);
    }

    @Override
    public <TReponse> Observable<TReponse> dispatch(Message<TReponse> message) {
        final Collection<MessageCaptor> captors = handlers.get(message.getClass());
        if (captors.size() == 0) {
            LOGGER.warn("Can't find handler for {}", message.getClass());
            captors.add(m -> Single.just(EMPTY));
        }

        LOGGER.debug("Executing captor for {}", message.getClass());
        return Observable.fromIterable(captors)
                .concatMap(c -> execute(message, c).toObservable());
    }


    private <TReponse> Single<TReponse> execute(Message<TReponse> message, MessageCaptor<Message<TReponse>, TReponse> messageCaptor) {
        Single<TReponse> response = Single.<TReponse>create(o -> {
            try {
                o.onSuccess(messageCaptor.execute(message));
            } catch (Exception e) {
                o.onError(e);
            }
        })
                .doOnSubscribe((d) -> synchronisations.forEach((synchro) -> synchro.beforeExecution(message)))
                .doOnSuccess((r) -> {
                    synchronisations.forEach(SynchronisationBus::afterExecution);
                    synchronisations.forEach(SynchronisationBus::atLast);
                })
                .doOnError((err) -> {
                    synchronisations.forEach(SynchronisationBus::onError);
                    synchronisations.forEach(SynchronisationBus::atLast);
                    logUnknownError(err);
                });
        if (isSync(message)) {
            return response;
        }
        return response
                .subscribeOn(Schedulers.computation());

    }

    private boolean isSync(Message<?> event) {
        return event.getClass().getAnnotation(Synchrone.class) != null;
    }

    private void logUnknownError(Throwable e) {
        if (!ValidationException.class.isAssignableFrom(e.getClass()) && !BusinessError.class.isAssignableFrom(e.getClass())) {
            LOGGER.error("Error while executing command", e);
        }
    }

    protected void addSynchronization(SynchronisationBus sync) {
        synchronisations.add(sync);
    }

    protected final static Logger LOGGER = LoggerFactory.getLogger(AsynchronousBus.class);
    protected final Multimap<Class<?>, MessageCaptor> handlers = ArrayListMultimap.create();
    private final List<SynchronisationBus> synchronisations = Lists.newArrayList();
    private static final Object EMPTY = new Object();
}
