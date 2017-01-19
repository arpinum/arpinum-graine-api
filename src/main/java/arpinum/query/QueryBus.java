package arpinum.query;

import arpinum.infrastructure.bus.AsynchronousBus;

import javax.inject.*;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
public class QueryBus extends AsynchronousBus {

    @Inject
    public QueryBus(Set<QuerySynchronization> synchronizations, Set<QueryHandler> handlers) {
        super(synchronizations, handlers);
    }

}
