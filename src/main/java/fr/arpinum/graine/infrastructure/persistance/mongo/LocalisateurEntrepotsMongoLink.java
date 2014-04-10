package fr.arpinum.graine.infrastructure.persistance.mongo;

import votreapplication.modele.LocalisateurEntrepots;

import javax.inject.Inject;


public class LocalisateurEntrepotsMongoLink extends LocalisateurEntrepots {

    @Inject
    public LocalisateurEntrepotsMongoLink(ContexteMongoLink contexteMongoLink) {
        this.contexteMongoLink = contexteMongoLink;
    }

    protected ContexteMongoLink contexte() {
        return contexteMongoLink;
    }

    private final ContexteMongoLink contexteMongoLink;
}
