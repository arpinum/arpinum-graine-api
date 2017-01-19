package arpinum.ddd;

public interface Repository<TId, TRacine extends AggregateRoot<TId>> {

    TRacine get(TId id);

    boolean exists(TId id);

    void add(TRacine racine);

    void delete(TRacine racine);
}
