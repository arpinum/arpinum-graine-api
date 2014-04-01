package fr.arpinum.graine.bus;


public interface SynchronisationBus {

    default void avantExecution(Commande<?> commande) {

    }

    default void surErreur() {

    }

    default void apresExecution() {

    }

    default void finalement() {

    }
}
