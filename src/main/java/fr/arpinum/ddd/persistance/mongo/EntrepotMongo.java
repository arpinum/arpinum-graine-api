package fr.arpinum.ddd.persistance.mongo;

import fr.arpinum.ddd.modele.Entrepot;
import fr.arpinum.ddd.modele.Racine;
import org.mongolink.MongoSession;

public abstract class EntrepotMongo<TId, TRacine extends Racine<TId>> implements Entrepot<TId, TRacine> {

    protected EntrepotMongo(MongoSession session) {
        this.session = session;
    }


    @Override
    public TRacine get(TId tId) {
        return null;
    }

    @Override
    public void ajoute(TRacine racine) {
        session.save(racine);
    }

    @Override
    public void supprime(TRacine racine) {

    }

    private final MongoSession session;
}
