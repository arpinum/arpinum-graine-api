package fr.arpinum.graine.infrastructure.persistance.memoire;

import fr.arpinum.graine.modele.AggregateWithUuid;
import fr.arpinum.graine.modele.RepositoryWithUuid;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public class MemoryRepositoryWithUuid<TRacine extends AggregateWithUuid> extends MemoryRepository<UUID, TRacine> implements RepositoryWithUuid<TRacine> {
}
