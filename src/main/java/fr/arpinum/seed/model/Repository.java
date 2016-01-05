package fr.arpinum.seed.model;

public interface Repository<TId, TRoot extends Aggregate<TId>> {

    TRoot get(TId id);

    void add(TRoot root);

    void delete(TRoot root);
}
