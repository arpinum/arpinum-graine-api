package fr.arpinum.graine.infrastructure.persistance.mongo;

import fr.arpinum.graine.modele.Repository;
import org.mongolink.MongoSession;

public class FakeRootRepository extends MongolinkRepository<String, FakeAggregate> implements Repository<String, FakeAggregate> {

    protected FakeRootRepository(MongoSession session) {
        super(session);
    }


}
