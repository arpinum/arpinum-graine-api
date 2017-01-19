package yourapp.infrastructure.repository;

import arpinum.ddd.BaseAggregate;
import arpinum.ddd.evenement.EventStore;
import arpinum.infrastructure.persistance.eventsourcing.UnitOfWork;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.function.Supplier;


public class DirectUnitOfWork extends UnitOfWork {
    public DirectUnitOfWork(EventStore eventStore) {
        super(eventStore, null);
    }

    @Override
    public void add(BaseAggregate<?> aggregate) {
        instances.put(aggregate.getClass(), aggregate);
    }

    @Override
    public <T extends BaseAggregate<?>> T get(Object id, Class<T> type, Supplier<T> loader) {
        return (T) instances.get(type).stream().filter(agg -> agg.getId().equals(id))
                .findFirst().orElseThrow(() -> new IllegalArgumentException());
    }

    @Override
    public void delete(BaseAggregate<?> aggregate) {
        instances.get(aggregate.getClass()).remove(aggregate);
    }

    private final Multimap<Class<?>, BaseAggregate<?>> instances = ArrayListMultimap.create();

}
