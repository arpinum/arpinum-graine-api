package arpinum.infrastructure.persistance.eventsourcing;

import arpinum.ddd.evenement.Event;
import arpinum.ddd.evenement.EventStore;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventStoreMemory implements EventStore {

    @Override
    public void save(Event<?>... event) {
        if (event.length == 0) {
            return;
        }
        events.putAll(event[0].targetType(), Arrays.asList(event));
    }

    @Override
    public <T> Stream<Event<T>> allOf(Class<T> type) {
        return events.get(type).stream().map(e -> (Event<T>) e);
    }

    @Override
    public <T, E extends Event<T>> Stream<E> allOfWithType(Class<T> type, Class<E> eventType) {
        return allOf(type).filter(e -> e.getClass().equals(eventType))
                .map(e -> (E) e);
    }

    @Override
    public <T> Stream<Event<T>> allOf(Object id, Class<T> type) {
        return allOf(type).filter(e -> e.getTargetId().equals(id));
    }

    @Override
    public <T> long count(Object id, Class<T> type) {
        return allOf(id, type).count();
    }


    @Override
    public void markAllAsDeleted(Object id, Class<?> type) {
        List<Event<?>> remaining = events.get(type).stream().filter(e -> !id.equals(e.getTargetId())).collect(Collectors.toList());
        events.replaceValues(type, remaining);
    }


    private ListMultimap<Class<?>, Event<?>> events = ArrayListMultimap.create();
}
