package fr.arpinum.seed.model;


import java.util.UUID;

public interface RepositoryWithUuid<TRoot extends AggregateWithUuid> extends Repository<UUID, TRoot> {
}
