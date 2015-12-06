package fr.arpinum.graine.modele.evenement;

import fr.arpinum.graine.infrastructure.bus.MessageCaptor;


@SuppressWarnings("UnusedDeclaration")
public interface EventCaptor<TEvenement extends Event> extends MessageCaptor<TEvenement, Void> {

    @Override
    default Void execute(TEvenement evenement) {
        executeEvent(evenement);
        return null;
    }

    void executeEvent(TEvenement evenement);
}
