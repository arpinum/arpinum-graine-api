package fr.arpinum.seed.query;

import fr.arpinum.seed.infrastructure.bus.Message;

@SuppressWarnings("UnusedDeclaration")
public class Query<TResponse> implements Message<TResponse> {

    public Query<TResponse> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Query<TResponse> skip(int passe) {
        this.skip = passe;
        return this;
    }

    public int limit() {
        return limit;
    }

    public int skip() {
        return skip;
    }

    protected int limit;
    protected int skip;
}
