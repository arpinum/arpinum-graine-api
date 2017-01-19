package arpinum.infrastructure.bus.event;

import arpinum.ddd.evenement.Event;
import arpinum.ddd.evenement.EventCaptor;
import arpinum.ddd.evenement.SynchronisationEvenement;
import arpinum.infrastructure.bus.AsynchronousBus;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Queue;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class AsyncEventBus extends AsynchronousBus implements CommandSynchronizedEventBus {

    @Inject
    public AsyncEventBus(Set<SynchronisationEvenement> synchronisations, Set<EventCaptor> handlers) {
        super(synchronisations, handlers);
        addSynchronization(this);
    }

    @Override
    public void afterExecution() {
        LOGGER.debug("Propagating events");
        while (!events.get().isEmpty()) {
            final Event event = events.get().poll();
            doPublishEvent(event);
        }
    }

    protected void doPublishEvent(Event event) {
        dispatch(event).subscribe();
    }

    @Override
    public void onError() {
        events.get().clear();
    }

    @Override
    public void publish(Event... events) {
        this.events.get().addAll(Arrays.asList(events));
    }

    private final ThreadLocal<Queue<Event>> events = ThreadLocal.withInitial((java.util.function.Supplier<Queue<Event>>) Lists::newLinkedList);
}
