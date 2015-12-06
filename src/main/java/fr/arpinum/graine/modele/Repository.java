package fr.arpinum.graine.modele;

public interface Repository<TId, TRacine extends Aggregate<TId>> {

    TRacine get(TId id);

    void add(TRacine racine);

    void delete(TRacine racine);
}
