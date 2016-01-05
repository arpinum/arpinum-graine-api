package yourapplication.infrastructure.persistence.mongo;

import fr.arpinum.seed.infrastructure.persistence.mongo.MongoLinkContext;
import yourapplication.model.RepositoriesLocator;

import javax.inject.Inject;


public class MongoLinkRepositoryLocator extends RepositoriesLocator {

    @Inject
    public MongoLinkRepositoryLocator(MongoLinkContext context) {
        this.contextMongoLink = context;
    }

    protected MongoLinkContext context() {
        return contextMongoLink;
    }

    private final MongoLinkContext contextMongoLink;
}
