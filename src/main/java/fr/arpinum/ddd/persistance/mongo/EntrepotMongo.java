package fr.arpinum.ddd.persistance.mongo;

import com.google.common.reflect.TypeToken;
import fr.arpinum.ddd.modele.Entrepot;
import fr.arpinum.ddd.modele.Racine;
import org.mongolink.MongoSession;

public abstract class EntrepotMongo<TId, TRacine extends Racine<TId>> implements Entrepot<TId, TRacine> {

    protected EntrepotMongo(MongoSession session) {
        this.session = session;
    }


    @Override
    public TRacine get(TId tId) {
        return getSession().get(tId, typeEntité());
    }

    @Override
    public void ajoute(TRacine racine) {
        getSession().save(racine);
    }

    @Override
    public void supprime(TRacine racine) {
        getSession().delete(racine);
    }

    protected Class<TRacine> typeEntité() {
        return (Class<TRacine>) typeToken.getRawType();
    }

    protected MongoSession getSession() {
        return session;
    }

    private final MongoSession session;
    private final TypeToken<TRacine> typeToken = new TypeToken<TRacine>(getClass()) {
    };
}
