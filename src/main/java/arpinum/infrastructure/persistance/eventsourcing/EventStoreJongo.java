package arpinum.infrastructure.persistance.eventsourcing;

import arpinum.ddd.evenement.Event;
import arpinum.ddd.evenement.EventStore;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EventStoreJongo implements EventStore {

    @Inject
    public EventStoreJongo(@Named("boundedcontext.name") String name, Jongo jongo) {
        this.jongo = jongo;
        this.collection = name + "_eventstore";
    }

    @Override
    public void save(Event<?>... events) {
        jongo.getCollection(collection).insert((Object[]) events);
    }

    @Override
    public <T> Stream<Event<T>> allOf(Class<T> type) {
        MongoCursor<Event> results = jongo.getCollection(collection).find("{targetType:#, _deleted:{$exists:false}}", type.getSimpleName()).as(Event.class);
        return StreamSupport.stream(results.spliterator(), false).map(e -> (Event<T>) e)
                .onClose(() -> closeQuietly(results));
    }

    @Override
    public <T, E extends Event<T>> Stream<E> allOfWithType(Class<T> type, Class<E> eventType) {
        MongoCursor<Event> results = jongo
                .getCollection(collection)
                .find("{targetType:#, _deleted:{$exists:false}, _class:#}", type.getSimpleName(), eventType.getName())
                .as(Event.class);
        return StreamSupport.stream(results.spliterator(), false).map(e -> (E) e)
                .onClose(() -> closeQuietly(results));
    }

    @Override
    public <T> Stream<Event<T>> allOf(Object id, Class<T> type) {
        MongoCursor<Event> results = jongo.getCollection(collection).find("{targetType:#, targetId:#, _delete:{$exists:false}}", type.getSimpleName(), id).as(Event.class);
        return StreamSupport.stream(results.spliterator(), false).map(e -> (Event<T>) e)
                .onClose(() -> closeQuietly(results));
    }

    @Override
    public <T> long count(Object id, Class<T> type) {
        return jongo.getCollection(collection).count("{targetType:#, targetId:#}", type.getSimpleName(), id);
    }

    @Override
    public void markAllAsDeleted(Object id, Class<?> type) {
        jongo.getCollection(collection).update("{targetId:#, targetType:#}", id, type.getSimpleName()).multi().with("{$set:{_deleted:true}}");
    }

    private void closeQuietly(MongoCursor<Event> results) {
        try {
            results.close();
        } catch (IOException e) {
            LOGGER.error("Error loading events", e);
        }
    }

    private final String collection;

    private Jongo jongo;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventStoreJongo.class);
}
