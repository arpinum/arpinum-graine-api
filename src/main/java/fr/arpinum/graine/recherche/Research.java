package fr.arpinum.graine.recherche;

import fr.arpinum.graine.infrastructure.bus.Message;

@SuppressWarnings("UnusedDeclaration")
public class Research<TReponse> implements Message<TReponse> {

    public Research<TReponse> limit(int limite) {
        this.limit = limite;
        return this;
    }

    public Research<TReponse> skip(int passe) {
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
