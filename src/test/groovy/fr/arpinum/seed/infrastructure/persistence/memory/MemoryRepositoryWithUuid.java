package fr.arpinum.seed.infrastructure.persistence.memory;

import fr.arpinum.seed.model.AggregateWithUuid;
import fr.arpinum.seed.model.RepositoryWithUuid;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public class MemoryRepositoryWithUuid<TRoot extends AggregateWithUuid> extends MemoryRepository<UUID, TRoot> implements RepositoryWithUuid<TRoot> {
}
