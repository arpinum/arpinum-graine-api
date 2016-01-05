package fr.arpinum.seed.infrastructure.persistence.mongo;

import fr.arpinum.seed.model.AggregateWithUuid;
import fr.arpinum.seed.model.RepositoryWithUuid;
import org.mongolink.MongoSession;

import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
public abstract class MongoLinkRepositoryWithUuid<TRacine extends AggregateWithUuid> extends MongolinkRepository<UUID, TRacine> implements RepositoryWithUuid<TRacine> {

    protected MongoLinkRepositoryWithUuid(MongoSession session) {
        super(session);
    }
}
