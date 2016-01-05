package fr.arpinum.seed.infrastructure.persistence.mongo;

import fr.arpinum.seed.command.CommandSynchronization;
import fr.arpinum.seed.infrastructure.bus.Message;
import fr.arpinum.seed.model.event.EventSynchronization;
import fr.arpinum.seed.query.QuerySynchronization;
import org.mongolink.*;
import org.slf4j.*;

import javax.inject.Inject;

public class MongoLinkContext implements CommandSynchronization, EventSynchronization, QuerySynchronization, MongoSessionProvider {


    @Inject
    public MongoLinkContext(MongoSessionManager sessionManager) {
        sessions = ThreadLocal.withInitial(sessionManager::createSession);
    }

    @Override
    public void beforeExecution(Message<?> message) {
        LOGGER.debug("Démarrage d'une session");
        sessions.get().start();
    }

    @Override
    public void afterExecution() {
        LOGGER.debug("Synchronisation avec MongoDB");
        sessions.get().flush();
    }

    @Override
    public void atLast() {
        LOGGER.debug("Arrêt de la session");
        sessions.get().stop();
        sessions.remove();
    }

    @Override
    public void onError() {
        LOGGER.debug("Nettoyage sur erreur de la session");
        sessions.get().clear();
    }

    @Override
    public MongoSession currentSession() {
        return sessions.get();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoLinkContext.class);
    private final ThreadLocal<MongoSession> sessions;
}
