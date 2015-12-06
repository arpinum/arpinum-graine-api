package fr.arpinum.graine.modele.evenement;


@SuppressWarnings("UnusedDeclaration")
public interface EventBus {

    void publish(Event... event);
}
