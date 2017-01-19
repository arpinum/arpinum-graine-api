package arpinum.ddd;


import arpinum.ddd.evenement.EventBus;

public interface AggregateRoot<TId> extends Entity<TId> {

    void flushEvents(EventBus bus);
}
