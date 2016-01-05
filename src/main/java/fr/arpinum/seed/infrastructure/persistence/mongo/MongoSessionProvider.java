package fr.arpinum.seed.infrastructure.persistence.mongo;

import org.mongolink.MongoSession;

public interface MongoSessionProvider {

    MongoSession currentSession();
}
