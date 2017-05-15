package arpinum.query;

import arpinum.infrastructure.bus.AsynchronousBus;
import com.google.common.util.concurrent.MoreExecutors;
import io.vavr.collection.List;

import javax.inject.Inject;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class QueryBus extends AsynchronousBus {

    @Inject
    public QueryBus(Set<QuerySynchronization> synchronizations, Set<QueryHandler> handlers) {
        super(List.ofAll(synchronizations), List.ofAll(handlers), MoreExecutors.newDirectExecutorService());
    }

}
