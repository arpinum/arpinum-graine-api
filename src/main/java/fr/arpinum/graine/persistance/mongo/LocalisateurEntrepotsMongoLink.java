package fr.arpinum.graine.persistance.mongo;

import fr.arpinum.graine.modele.LocalisateurEntrepots;

import javax.inject.Inject;

@SuppressWarnings("UnusedDeclaration")
public abstract class LocalisateurEntrepotsMongoLink implements LocalisateurEntrepots {

    @Inject
    public LocalisateurEntrepotsMongoLink(ContexteMongoLink contexteMongoLink) {
        this.contexteMongoLink = contexteMongoLink;
    }

    protected ContexteMongoLink contexte() {
        return contexteMongoLink;
    }

    private final ContexteMongoLink contexteMongoLink;
}
