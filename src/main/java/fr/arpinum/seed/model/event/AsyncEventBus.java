package fr.arpinum.seed.model.event;

import com.google.common.collect.*;
import fr.arpinum.seed.command.CommandSynchronization;
import fr.arpinum.seed.infrastructure.bus.AsynchronousBus;

import javax.inject.*;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
public class AsyncEventBus extends AsynchronousBus implements EventBus, CommandSynchronization, EventSynchronization {

    @Inject
    public AsyncEventBus(Set<EventSynchronization> synchronisations, Set<EventCaptor> handlers) {
        super(synchronisations, handlers);
        addSynchronization(this);
    }

    @Override
    public void afterExecution() {
        LOGGER.debug("Propagation des évènements");
        while (!events.get().isEmpty()) {
            final Event event = events.get().poll();
            if (isSync(event)) {
                sendAndWaitForResponse(event);
            } else {
                send(event);
            }
        }
    }

    private boolean isSync(Event event) {
        return event.getClass().getAnnotation(Synchronous.class) != null;

    }

    @Override
    public void onError() {
        events.get().clear();
    }

    @Override
    public void publish(Event... event) {
        events.get().addAll(Arrays.asList(event));
    }

    private final ThreadLocal<Queue<Event>> events = ThreadLocal.withInitial((java.util.function.Supplier<Queue<Event>>) Lists::newLinkedList);
}
