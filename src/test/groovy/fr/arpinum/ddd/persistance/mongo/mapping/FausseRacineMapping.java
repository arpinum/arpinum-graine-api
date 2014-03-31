package fr.arpinum.ddd.persistance.mongo.mapping;

import fr.arpinum.ddd.persistance.mongo.FausseRacine;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class FausseRacineMapping extends AggregateMap<FausseRacine> {

    public FausseRacineMapping() {
        super(FausseRacine.class);
    }

    @Override
    public void map() {
        id().onProperty(element().getId()).natural();
    }
}
