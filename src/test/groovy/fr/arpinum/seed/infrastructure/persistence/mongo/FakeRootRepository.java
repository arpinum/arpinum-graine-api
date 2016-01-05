package fr.arpinum.seed.infrastructure.persistence.mongo;

import fr.arpinum.seed.model.Repository;
import org.mongolink.MongoSession;

public class FakeRootRepository extends MongolinkRepository<String, FakeAggregate> implements Repository<String, FakeAggregate> {

    protected FakeRootRepository(MongoSession session) {
        super(session);
    }


}
