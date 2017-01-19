package arpinum.infrastructure.persistance.eventsourcing;


import arpinum.ddd.BaseAggregateWithUuid;
import arpinum.ddd.evenement.Event;
import arpinum.ddd.evenement.EventBus;
import arpinum.ddd.evenement.EventSourceHandler;
import arpinum.ddd.evenement.EventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;

public class EventSourcedRepositoryTest {

    private EventStore eventStore;
    private EventBus bus;
    private EventSourcedRepository<UUID, EventSourcedAggregate> repository;
    private UnitOfWork unitOfWork;

    @Before
    public void setUp() throws Exception {
        eventStore = new EventStoreMemory();
        bus = mock(EventBus.class);
        unitOfWork = new UnitOfWork(eventStore, bus);
        repository = new AggregateRepository(unitOfWork, eventStore);
    }

    @Test
    public void returns_aggregate() throws Exception {
        UUID targetId = UUID.randomUUID();
        eventStore.save(new CreatedEvent(targetId));

        EventSourcedAggregate aggregate = repository.get(targetId);

        assertThat(aggregate).isNotNull();
        assertThat(aggregate.getId()).isEqualTo(targetId);
    }

    @Test
    public void says_it_exists() throws Exception {
        UUID targetId = UUID.randomUUID();
        eventStore.save(new CreatedEvent(targetId));

        boolean exists = repository.exists(targetId);

        assertThat(exists).isTrue();
    }

    @Test
    public void mark_events_as_deleted() throws Exception {
        UUID targetId = UUID.randomUUID();
        CreatedEvent event = new CreatedEvent(targetId);
        eventStore.save(event);
        EventSourcedAggregate aggregate = new EventSourcedAggregate(targetId);
        aggregate.handle(event);
        aggregate.genEvent(event);
        repository.add(aggregate);

        repository.delete(aggregate);
        unitOfWork.afterExecution();

        assertThat(eventStore.count(targetId, EventSourcedAggregate.class)).isEqualTo(0);
    }

    @Test
    public void adding_adds_to_unit_of_work() throws Exception {
        UUID targetId = UUID.randomUUID();
        EventSourcedAggregate aggregate = new EventSourcedAggregate(targetId);
        CreatedEvent event = new CreatedEvent(targetId);
        aggregate.genEvent(event);

        repository.add(aggregate);

        EventSourcedAggregate instance = unitOfWork.get(targetId, EventSourcedAggregate.class, () -> null);
        assertThat(instance).isEqualTo(aggregate);
    }

    public static class EventSourcedAggregate extends BaseAggregateWithUuid {

        public EventSourcedAggregate() {
        }

        public EventSourcedAggregate(UUID targetId) {
            super(targetId);
        }

        @EventSourceHandler
        public void handle(CreatedEvent event) {
            this.setId((UUID) event.getTargetId());
        }

        public void genEvent(Event event) {
            pushEvent(event);
        }
    }

    public static class CreatedEvent extends Event<EventSourcedAggregate> {

        public CreatedEvent(Object targetId) {
            super(targetId);
        }

    }

    public static class AggregateRepository extends EventSourcedRepository<UUID, EventSourcedAggregate> {

        public AggregateRepository(UnitOfWork unitOfWork, EventStore eventStore) {
            super(unitOfWork, eventStore);
        }
    }
}
