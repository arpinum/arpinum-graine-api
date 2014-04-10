package fr.arpinum.graine.modele.evenement;

import fr.arpinum.graine.infrastructure.bus.BusAsynchrone;
import fr.arpinum.graine.infrastructure.bus.HandlerMessage;
import fr.arpinum.graine.infrastructure.bus.SynchronisationBus;

import javax.inject.Inject;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class BusEvenementAsynchrone extends BusAsynchrone implements BusEvenement {

    @Inject
    public BusEvenementAsynchrone(Set<? extends SynchronisationBus> synchronisations, Set<? extends HandlerMessage> handlers) {
        super(synchronisations, handlers);
    }

    @Override
    public void publie(Evenement evenement) {
        poste(evenement);
    }
}
