package fr.arpinum.ddd.commande;


public interface SynchronisationBus {

    void avantExecution(Commande<?> commande);

    void apresExecution();

    void finalement();
}
