package fr.arpinum.seed.infrastructure.bus;


public interface SynchronizationBus {

    default void beforeExecution(Message<?> message) {

    }

    default void onError() {

    }

    default void afterExecution() {

    }

    default void atLast() {

    }
}
