package fr.arpinum.ddd.persistance.mongo;

import fr.arpinum.ddd.modele.Entrepot;
import org.mongolink.MongoSession;

public class EntrepotFausseRacine extends EntrepotMongo<String, FausseRacine> implements Entrepot<String, FausseRacine> {

    protected EntrepotFausseRacine(MongoSession session) {
        super(session);
    }


}
