package arpinum.infrastructure.bus.event;

import arpinum.command.SynchronisationCommande;
import arpinum.ddd.evenement.EventBus;
import arpinum.ddd.evenement.SynchronisationEvenement;
import arpinum.infrastructure.bus.Bus;

public interface CommandSynchronizedEventBus extends Bus, EventBus, SynchronisationCommande, SynchronisationEvenement {
}
