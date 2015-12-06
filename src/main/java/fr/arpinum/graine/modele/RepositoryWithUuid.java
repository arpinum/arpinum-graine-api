package fr.arpinum.graine.modele;


import java.util.UUID;

public interface RepositoryWithUuid<TRacine extends AggregateWithUuid> extends Repository<UUID, TRacine> {
}
