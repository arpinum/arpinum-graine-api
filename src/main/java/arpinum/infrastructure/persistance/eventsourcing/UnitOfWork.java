package arpinum.infrastructure.persistance.eventsourcing;


import arpinum.command.SynchronisationCommande;
import arpinum.ddd.BaseAggregate;
import arpinum.ddd.evenement.EventBus;
import arpinum.ddd.evenement.EventStore;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Supplier;

public class UnitOfWork implements SynchronisationCommande {

    @Inject
    public UnitOfWork(EventStore eventStore, EventBus bus) {
        this.eventStore = eventStore;
        this.bus = bus;
    }

    public <T extends BaseAggregate<?>> T get(Object id, Class<T> type, Supplier<T> loader) {
        Key key = new Key(id, type);
        currentUnit.get().computeIfAbsent(key, (k) -> new AggregateHolder(loader.get()));
        return (T) currentUnit.get().get(key).instance;
    }

    public void add(BaseAggregate<?> aggregate) {
        currentUnit.get().put(new Key(aggregate.getId(), aggregate.getClass()), new AggregateHolder(aggregate));
    }

    @Override
    public void onError() {
        currentUnit.get().clear();
    }


    @Override
    public void afterExecution() {
        currentUnit.get().values().forEach(tRoot -> tRoot.flush());
    }

    @Override
    public void atLast() {
        currentUnit.get().clear();
    }

    private ThreadLocal<Map<Key, AggregateHolder>> currentUnit = ThreadLocal.withInitial(() -> Maps.newConcurrentMap());

    public void delete(BaseAggregate<?> aggregate) {
        currentUnit.get().computeIfPresent(new Key(aggregate.getId(), aggregate.getClass()), (k, v) -> v.markForDeletion());
    }


    private class Key {

        private Key(Object id, Class<?> type) {
            this.id = id;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equal(id, key.id) &&
                    Objects.equal(type, key.type);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id, type);
        }

        final Object id;

        final Class<?> type;
    }

    private class AggregateHolder {
        public AggregateHolder(BaseAggregate<?> instance) {
            this.instance = instance;
        }

        AggregateHolder markForDeletion() {
            state = state.markForDeletion();
            return this;
        }

        void flush() {
            state.flush(instance, eventStore, bus);

        }

        private AggregateState state = AggregateState.DIRTY;

        private BaseAggregate<?> instance;
    }

    private enum AggregateState {
        DELETED {
            @Override
            AggregateState markForDeletion() {
                return this;
            }

            @Override
            void flush(BaseAggregate<?> instance, EventStore eventStore, EventBus bus) {
                instance.flushEvents((e) -> {
                    eventStore.save(e);
                    eventStore.markAllAsDeleted(instance.getId(), instance.getClass());
                    bus.publish(e);
                });
            }


        }, DIRTY {
            @Override
            AggregateState markForDeletion() {
                return DELETED;
            }

            @Override
            void flush(BaseAggregate<?> instance, EventStore eventStore, EventBus bus) {
                instance.flushEvents((e) -> {
                    eventStore.save(e);
                    bus.publish(e);
                });
            }
        };

        abstract AggregateState markForDeletion();

        abstract void flush(BaseAggregate<?> instance, EventStore eventStore, EventBus bus);
    }

    private final EventStore eventStore;
    private final EventBus bus;
}
