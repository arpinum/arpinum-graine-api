package fr.arpinum.seed.persistance.mongo;

import fr.arpinum.seed.modele.Racine;

public class FausseRacine implements Racine<String> {

    @SuppressWarnings("UnusedDeclaration")
    public FausseRacine() {
    }

    FausseRacine(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String id;
}