package fr.arpinum.graine.recherche;

import org.jongo.Jongo;

@SuppressWarnings("UnusedDeclaration")
public abstract class HandlerRechercheJongo<TRecherche extends Recherche<TReponse>, TReponse> implements HandlerRecherche<TRecherche, TReponse> {

    protected HandlerRechercheJongo(Jongo jongo) {
        this.jongo = jongo;
    }

    private final Jongo jongo;
}
