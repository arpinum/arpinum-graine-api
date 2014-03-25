package fr.arpinum.ddd.commande;


public interface SynchronisationBus {

    void avantExecution();

    void apresExecution();

    void finalement();
}
