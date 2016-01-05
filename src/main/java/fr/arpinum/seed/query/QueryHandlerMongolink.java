package fr.arpinum.seed.query;

import fr.arpinum.seed.infrastructure.persistence.mongo.MongoSessionProvider;
import org.mongolink.MongoSession;

import javax.inject.Inject;

public abstract class QueryHandlerMongolink<TQuery extends Query<TResponse>, TResponse> implements QueryHandler<TQuery, TResponse> {

    @Inject
    public void initialize(MongoSessionProvider context) {
        this.context = context;
    }

    public MongoSession session() {
        return context.currentSession();
    }

    private MongoSessionProvider context;
}
