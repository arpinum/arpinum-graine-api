package arpinum.infrastructure.bus;


public interface SynchronisationBus {

    default void beforeExecution(Message<?> message) {

    }

    default void onError() {

    }

    default void afterExecution() {

    }

    default void atLast() {

    }
}
