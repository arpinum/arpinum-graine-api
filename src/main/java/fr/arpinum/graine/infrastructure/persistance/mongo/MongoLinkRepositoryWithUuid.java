package fr.arpinum.graine.infrastructure.persistance.mongo;

import fr.arpinum.graine.modele.AggregateWithUuid;
import fr.arpinum.graine.modele.RepositoryWithUuid;
import org.mongolink.MongoSession;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public abstract class MongoLinkRepositoryWithUuid<TRacine extends AggregateWithUuid> extends MongolinkRepository<UUID, TRacine> implements RepositoryWithUuid<TRacine> {

    protected MongoLinkRepositoryWithUuid(MongoSession session) {
        super(session);
    }
}
