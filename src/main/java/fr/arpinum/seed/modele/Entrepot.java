package fr.arpinum.seed.modele;


public interface Entrepot<TId, TRacine extends Racine<TId>> {

    TRacine get(TId id);

    void ajoute(TRacine racine);

    void supprime(TRacine racine);
}
