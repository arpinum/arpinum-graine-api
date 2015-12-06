package votreapplication.infrastructure.persistance.mongo;

import fr.arpinum.graine.infrastructure.persistance.mongo.MongoLinkContext;
import votreapplication.modele.LocalisateurEntrepots;

import javax.inject.Inject;


public class LocalisateurEntrepotsMongoLink extends LocalisateurEntrepots {

    @Inject
    public LocalisateurEntrepotsMongoLink(MongoLinkContext contexteMongoLink) {
        this.contexteMongoLink = contexteMongoLink;
    }

    protected MongoLinkContext contexte() {
        return contexteMongoLink;
    }

    private final MongoLinkContext contexteMongoLink;
}
