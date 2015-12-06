package fr.arpinum.graine.infrastructure.persistance.mongo.mapping;

import fr.arpinum.graine.infrastructure.persistance.mongo.FakeAggregate;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class FausseRacineMapping extends AggregateMap<FakeAggregate> {

    @Override
    public void map() {
        id().onProperty(e -> e.getId()).natural();
    }
}
