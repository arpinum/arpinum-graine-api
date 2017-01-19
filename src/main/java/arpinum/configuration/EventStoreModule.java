package arpinum.configuration;


import arpinum.ddd.evenement.EventStore;
import arpinum.infrastructure.persistance.eventsourcing.EventStoreJongo;
import arpinum.infrastructure.persistance.eventsourcing.UnitOfWork;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class EventStoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventStore.class).to(EventStoreJongo.class).in(Singleton.class);
        bind(UnitOfWork.class).in(Singleton.class);
    }
}
