package arpinum.ddd.event;


@SuppressWarnings("UnusedDeclaration")
public interface EventBus {

    void publish(Event... events);
}
