package arpinum.ddd.evenement;

import java.util.stream.Stream;

public interface EventStore {

    void save(Event<?>... event);

    <T> Stream<Event<T>> allOf(Class<T> type);

    <T, E extends Event<T>> Stream<E> allOfWithType(Class<T> type, Class<E> eventType);

    <T> Stream<Event<T>> allOf(Object id, Class<T> type);

    <T> long count(Object id, Class<T> type);

    void markAllAsDeleted(Object id, Class<?> type);
}
