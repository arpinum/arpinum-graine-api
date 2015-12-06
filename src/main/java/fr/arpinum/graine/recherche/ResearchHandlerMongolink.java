package fr.arpinum.graine.recherche;

import fr.arpinum.graine.infrastructure.persistance.mongo.MongoSessionProvider;
import org.mongolink.MongoSession;

import javax.inject.Inject;

public abstract class ResearchHandlerMongolink<TRecherche extends Research<TReponse>, TReponse> implements ResearchHandler<TRecherche, TReponse>  {

    @Inject
    public void initialize(MongoSessionProvider context) {
        this.context = context;
    }

    public MongoSession session() {
        return context.currentSession();
    }

    private MongoSessionProvider context;
}
