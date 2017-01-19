package arpinum.ddd.evenement;


@SuppressWarnings("UnusedDeclaration")
public interface EventBus {

    void publish(Event... events);
}
