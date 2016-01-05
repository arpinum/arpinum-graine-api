package fr.arpinum.seed.query;

import fr.arpinum.seed.infrastructure.bus.AsynchronousBus;

import javax.inject.*;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
public class QueryBus extends AsynchronousBus {

    @Inject
    public QueryBus(Set<QuerySynchronization> synchronizations, Set<QueryHandler> handlers) {
        super(synchronizations, handlers);
    }

}
