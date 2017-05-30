package arpinum.infrastructure.persistance.eventsourcing;

import arpinum.command.Command;
import arpinum.command.CommandMiddleware;
import arpinum.ddd.event.Event;
import arpinum.ddd.event.EventStore;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

import javax.inject.Inject;
import java.util.function.Supplier;


public class EventStoreMiddleware implements CommandMiddleware {

    private EventStore eventStore;

    @Inject
    public EventStoreMiddleware(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Tuple2<?, Seq<Event>> intercept(Command<?> message, Supplier<Tuple2<?, Seq<Event>>> next) {
        Tuple2<?, Seq<Event>> result = next.get();
        eventStore.save(result.apply((r, e) -> e)
                .map(e -> (Event) e));
        return result;
    }
}
