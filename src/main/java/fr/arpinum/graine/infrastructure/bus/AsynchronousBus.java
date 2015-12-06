package fr.arpinum.graine.infrastructure.bus;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import fr.arpinum.graine.commande.*;
import fr.arpinum.graine.modele.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class AsynchronousBus implements Bus {

    public AsynchronousBus(Set<? extends SynchronisationBus> synchronisations, Set<? extends MessageCaptor> handlers) {
        for (MessageCaptor handler : handlers) {
            this.handlers.put(handler.typeCommande(), handler);
        }
        this.synchronisations.addAll(synchronisations);
    }

    @Override
    public <TReponse> ExecutionResult<TReponse> sendAndWaitForResponse(Message<TReponse> message) {
        return Futures.getUnchecked(doSend(message, MoreExecutors.sameThreadExecutor()));
    }

    protected <TReponse> ListenableFuture<ExecutionResult<TReponse>> doSend(Message<TReponse> message, ListeningExecutorService executorService) {
        final Collection<MessageCaptor> captors = handlers.get(message.getClass());
        if (captors.size() == 0) {
            LOGGER.warn("Impossible de trouver un handler pour {}", message.getClass());
            return Futures.immediateFuture(ExecutionResult.error(new ErreurBus("Impossible de trouver un handler")));
        }
        LOGGER.debug("Exécution capteur pour {}", message.getClass());
        List<ListenableFuture<ExecutionResult<TReponse>>> futures = Lists.newArrayList();
        captors.forEach(c -> futures.add(executorService.submit(execute(message, c)))
        );
        return futures.get(0);
    }

    private <TReponse> Callable<ExecutionResult<TReponse>> execute(Message<TReponse> message, MessageCaptor<Message<TReponse>, TReponse> messageCaptor) {
        return () -> {
            try {
                synchronisations.forEach((synchro) -> synchro.beforeExecution(message));
                final TReponse reponse = messageCaptor.execute(message);
                synchronisations.forEach(SynchronisationBus::afterExecution);
                return ExecutionResult.success(reponse);
            } catch (Throwable e) {
                synchronisations.forEach(SynchronisationBus::onError);
                logUnknownError(e);
                return ExecutionResult.error(e);
            } finally {
                synchronisations.forEach(SynchronisationBus::atLast);
            }
        };
    }

    private void logUnknownError(Throwable e) {
        if (!ValidationException.class.isAssignableFrom(e.getClass()) && !BusinessError.class.isAssignableFrom(e.getClass())) {
            LOGGER.error("Erreur sur exécution de la commande", e);
        }
    }

    @Override
    public <TReponse> ListenableFuture<ExecutionResult<TReponse>> send(Message<TReponse> message) {
        return doSend(message, this.executorService);
    }

    public void setExecutor(ExecutorService executor) {
        this.executorService = MoreExecutors.listeningDecorator(executor);
    }

    protected void addSynchronization(SynchronisationBus sync) {
        synchronisations.add(sync);
    }

    protected final static Logger LOGGER = LoggerFactory.getLogger(AsynchronousBus.class);
    protected final Multimap<Class<?>, MessageCaptor> handlers = ArrayListMultimap.create();
    private final List<SynchronisationBus> synchronisations = Lists.newArrayList();
    protected ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
            Executors.newCachedThreadPool(
                    new ThreadFactoryBuilder().setNameFormat(getClass().getSimpleName() + "-%d").build())
    );
}
