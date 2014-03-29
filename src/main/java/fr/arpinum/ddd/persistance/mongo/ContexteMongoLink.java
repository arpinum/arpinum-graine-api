package fr.arpinum.ddd.persistance.mongo;

import fr.arpinum.ddd.bus.Commande;
import fr.arpinum.ddd.bus.SynchronisationBus;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;

import javax.inject.Inject;

public class ContexteMongoLink implements SynchronisationBus {


    @Inject
    public ContexteMongoLink(MongoSessionManager sessionManager) {
        sessions = ThreadLocal.withInitial(sessionManager::createSession);
    }

    @Override
    public void avantExecution(Commande<?> commande) {
        sessions.get().start();
    }

    @Override
    public void apresExecution() {

    }

    @Override
    public void finalement() {
        sessions.get().stop();
    }

    public MongoSession sessionCourante() {
        return sessions.get();
    }

    private final ThreadLocal<MongoSession> sessions;

}
