package arpinum.infrastructure.persistance.eventsourcing;

import arpinum.ddd.BaseAggregate;
import arpinum.ddd.Repository;
import arpinum.ddd.evenement.Event;
import arpinum.ddd.evenement.EventSourceHandler;
import arpinum.ddd.evenement.EventStore;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class EventSourcedRepository<TId, TRoot extends BaseAggregate<TId>> implements Repository<TId, TRoot> {

    public EventSourcedRepository(UnitOfWork unitOfWork, EventStore eventStore) {
        this.unitOfWork = unitOfWork;
        this.eventStore = eventStore;
        for (Method method : typeToken.getRawType().getMethods()) {
            if (method.getAnnotationsByType(EventSourceHandler.class).length > 0) {
                handlers.put(method.getParameters()[0].getType(), method);
            }
        }
        if (handlers.size() == 0) {
            LoggerFactory.getLogger(EventSourcedRepository.class).warn("No event handlers on {}", typeToken);
        }
    }

    @Override
    public TRoot get(TId tId) {
        return unitOfWork.get(tId, type(), () -> this.load(tId));
    }

    private TRoot load(TId tId) {
        Class<TRoot> type = (Class<TRoot>) typeToken.getRawType();
        if (eventStore.count(tId, type) == 0) {
            throw new IllegalArgumentException(String.format("Aggregate not found %s with id %s", typeToken, tId));
        }
        TRoot tRoot;
        try (Stream<Event<TRoot>> events = eventStore.allOf(tId, type)) {
            tRoot = createAggregateInstance();
            invokeHandlers(events, tRoot);
        }
        return tRoot;
    }

    private Class<TRoot> type() {
        return (Class<TRoot>) typeToken.getRawType();
    }

    private TRoot createAggregateInstance() {
        try {
            return (TRoot) typeToken.getRawType().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new EventStoreException(e);
        }
    }

    private void invokeHandlers(Stream<Event<TRoot>> events, TRoot tRoot) {
        events.forEach(e -> Optional.ofNullable(handlers.get(e.getClass())).ifPresent(h -> {
            try {
                h.invoke(tRoot, e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                throw new EventStoreException(e1);
            }
        }));
    }

    @Override
    public boolean exists(TId tId) {
        return eventStore.count(tId, typeToken.getRawType()) > 0;
    }

    @Override
    public void add(TRoot tRoot) {
        unitOfWork.add(tRoot);
    }

    @Override
    public void delete(TRoot tRoot) {
        unitOfWork.delete(tRoot);
    }

    private UnitOfWork unitOfWork;
    protected EventStore eventStore;
    private Map<Class<?>, Method> handlers = Maps.newConcurrentMap();
    private final TypeToken<TRoot> typeToken = new TypeToken<TRoot>(getClass()) {
    };
}
