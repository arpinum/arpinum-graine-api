package fr.arpinum.seed.infrastructure.persistence.mongo;

import fr.arpinum.seed.model.Aggregate;

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