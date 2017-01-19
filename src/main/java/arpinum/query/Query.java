package arpinum.query;


import arpinum.infrastructure.bus.Message;

@SuppressWarnings("UnusedDeclaration")
public class Query<TReponse> implements Message<TReponse> {

    public Query<TReponse> limit(int limite) {
        this.limit = limite;
        return this;
    }

    public Query<TReponse> skip(int passe) {
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
