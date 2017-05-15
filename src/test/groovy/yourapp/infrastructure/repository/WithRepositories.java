package yourapp.infrastructure.repository;

import arpinum.infrastructure.persistance.eventsourcing.EventStoreMemory;
import org.junit.rules.ExternalResource;
import yourapp.infrastructure.repository.eventsource.RepositoriesEventSourced;
import yourapp.model.Repositories;

public class WithRepositories extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        eventStore = new EventStoreMemory();
        Repositories.initialize(new RepositoriesEventSourced(eventStore));
    }

    @Override
    protected void after() {
        Repositories.initialize(null);
    }

    EventStoreMemory eventStore;
}
