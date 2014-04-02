package fr.arpinum.graine.infrastructure.persistance.mongo;

import fr.arpinum.graine.commande.SynchronisationCommande;
import fr.arpinum.graine.infrastructure.bus.Message;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;

import javax.inject.Inject;

public class ContexteMongoLink implements SynchronisationCommande {


    @Inject
    public ContexteMongoLink(MongoSessionManager sessionManager) {
        sessions = ThreadLocal.withInitial(sessionManager::createSession);
    }

    @Override
    public void avantExecution(Message<?> message) {
        sessions.get().start();
    }

    @Override
    public void finalement() {
        sessions.get().stop();
        sessions.remove();
    }

    @Override
    public void surErreur() {
        sessions.get().clear();
    }

    public MongoSession sessionCourante() {
        return sessions.get();
    }

    private final ThreadLocal<MongoSession> sessions;

}
