package fr.arpinum.bus;


public interface SynchronisationBus {

    void avantExecution(Commande<?> commande);

    void apresExecution();

    void finalement();
}
