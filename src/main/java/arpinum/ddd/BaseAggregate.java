package arpinum.ddd;

import arpinum.ddd.evenement.Event;
import arpinum.ddd.evenement.EventBus;
import com.google.common.collect.Lists;

import java.util.LinkedList;

@SuppressWarnings("UnusedDeclaration")
public abstract class BaseAggregate<TId> extends BaseEntity<TId> implements AggregateRoot<TId> {

    protected BaseAggregate() {
    }

    protected BaseAggregate(TId tId) {
        super(tId);
    }

    @Override
    public void flushEvents(EventBus bus) {
        bus.publish(events.toArray(new Event[0]));
        events.clear();
    }

    protected void pushEvent(Event event) {
        events.add(event);
    }

    private LinkedList<Event> events = Lists.newLinkedList();
}
