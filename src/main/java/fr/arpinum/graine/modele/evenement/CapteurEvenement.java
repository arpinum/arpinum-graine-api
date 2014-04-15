package fr.arpinum.graine.modele.evenement;

import fr.arpinum.graine.infrastructure.bus.CapteurMessage;


@SuppressWarnings("UnusedDeclaration")
public interface CapteurEvenement<TEvenement extends Evenement> extends CapteurMessage<TEvenement, Void> {

    @Override
    default Void execute(TEvenement evenement) {
        executeEvenement(evenement);
        return null;
    }

    void executeEvenement(TEvenement evenement);
}
