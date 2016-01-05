package fr.arpinum.seed.infrastructure.bus;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import fr.arpinum.seed.command.ExceptionValidation;
import fr.arpinum.seed.model.BusinessError;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class AsynchronousBus implements Bus {

    public AsynchronousBus(Set<? extends SynchronizationBus> synchronizations, Set<? extends MessageCaptor> handlers) {
        for (MessageCaptor handler : handlers) {
            this.handlers.put(handler.typeCommand(), handler);
        }
        this.synchronizations.addAll(synchronizations);
    }

    @Override
    public <TResponse> ExecutionResult<TResponse> sendAndWaitForResponse(Message<TResponse> message) {
        return Futures.getUnchecked(doSend(message, MoreExecutors.sameThreadExecutor()));
    }

    protected <TRseponse> ListenableFuture<ExecutionResult<TRseponse>> doSend(Message<TRseponse> message, ListeningExecutorService executorService) {
        final Collection<MessageCaptor> captors = handlers.get(message.getClass());
        if (captors.size() == 0) {
            LOGGER.warn("Impossible to find a handler for {}", message.getClass());
            return Futures.immediateFuture(ExecutionResult.error(new BusError("Impossible to find a handler")));
        }
        LOGGER.debug("Captor execution for {}", message.getClass());
        List<ListenableFuture<ExecutionResult<TRseponse>>> futures = Lists.newArrayList();
        captors.forEach(c -> futures.add(executorService.submit(execute(message, c)))
        );
        return futures.get(0);
    }

    private <TResponse> Callable<ExecutionResult<TResponse>> execute(Message<TResponse> message, MessageCaptor<Message<TResponse>, TResponse> messageCaptor) {
        return () -> {
            try {
                synchronizations.forEach((synchro) -> synchro.beforeExecution(message));
                final TResponse response = messageCaptor.execute(message);
                synchronizations.forEach(SynchronizationBus::afterExecution);
                return ExecutionResult.success(response);
            } catch (Throwable e) {
                synchronizations.forEach(SynchronizationBus::onError);
                logUnknownError(e);
                return ExecutionResult.error(e);
            } finally {
                synchronizations.forEach(SynchronizationBus::atLast);
            }
        };
    }

    private void logUnknownError(Throwable e) {
        if (!ExceptionValidation.class.isAssignableFrom(e.getClass()) && !BusinessError.class.isAssignableFrom(e.getClass())) {
            LOGGER.error("Command execution error", e);
        }
    }

    @Override
    public <TResponse> ListenableFuture<ExecutionResult<TResponse>> send(Message<TResponse> message) {
        return doSend(message, this.executorService);
    }

    public void setExecutor(ExecutorService executor) {
        this.executorService = MoreExecutors.listeningDecorator(executor);
    }

    protected void addSynchronization(SynchronizationBus sync) {
        synchronizations.add(sync);
    }

    protected final static Logger LOGGER = LoggerFactory.getLogger(AsynchronousBus.class);
    protected final Multimap<Class<?>, MessageCaptor> handlers = ArrayListMultimap.create();
    private final List<SynchronizationBus> synchronizations = Lists.newArrayList();
    protected ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
            Executors.newCachedThreadPool(
                    new ThreadFactoryBuilder().setNameFormat(getClass().getSimpleName() + "-%d").build())
    );
}
