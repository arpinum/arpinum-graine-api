package fr.arpinum.graine.infrastructure.persistance.mongo;

import org.mongolink.MongoSession;

public interface MongoSessionProvider {

    MongoSession currentSession();
}
