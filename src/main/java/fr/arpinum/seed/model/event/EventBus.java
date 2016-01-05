package fr.arpinum.seed.model.event;


@SuppressWarnings("UnusedDeclaration")
public interface EventBus {

    void publish(Event... event);
}
