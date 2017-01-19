package arpinum.infrastructure.persistance.eventsourcing;


import arpinum.ddd.BaseAggregate;
import arpinum.ddd.evenement.EventBus;
import arpinum.ddd.evenement.EventStore;

import java.util.UUID;

public class EventSourcedRepositoryWithUuid<TRoot extends BaseAggregate<UUID>> extends EventSourcedRepository<UUID, TRoot> {

    public EventSourcedRepositoryWithUuid(UnitOfWork unitOfWork, EventStore eventStore) {
        super(unitOfWork, eventStore);
    }
}
