package fr.arpinum.ddd.persistance.mongo;

import fr.arpinum.ddd.modele.Racine;

public class FausseRacine implements Racine<String> {

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