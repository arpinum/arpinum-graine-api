package fr.arpinum.graine.infrastructure.persistance.mongo;

import fr.arpinum.graine.modele.Aggregate;

public class FakeAggregate implements Aggregate<String> {

    @SuppressWarnings("UnusedDeclaration")
    public FakeAggregate() {
    }

    FakeAggregate(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String id;
}