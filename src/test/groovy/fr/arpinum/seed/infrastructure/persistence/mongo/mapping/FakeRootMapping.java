package fr.arpinum.seed.infrastructure.persistence.mongo.mapping;

import fr.arpinum.seed.infrastructure.persistence.mongo.FakeAggregate;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class FakeRootMapping extends AggregateMap<FakeAggregate> {

    @Override
    public void map() {
        id().onProperty(e -> e.getId()).natural();
    }
}
