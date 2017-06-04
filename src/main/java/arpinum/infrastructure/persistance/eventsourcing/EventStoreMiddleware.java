package arpinum.infrastructure.persistance.eventsourcing;

import arpinum.command.Command;
import arpinum.command.CommandBus;
import arpinum.command.CommandMiddleware;
import arpinum.ddd.event.Event;
import arpinum.ddd.event.EventStore;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.concurrent.Future;

import javax.inject.Inject;
import java.util.function.Supplier;


public class EventStoreMiddleware implements CommandMiddleware {

    private EventStore eventStore;

    @Inject
    public EventStoreMiddleware(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public <T> Future<Tuple2<T, Seq<Event>>> intercept(CommandBus bus, Command<T> message, Supplier<Future<Tuple2<T, Seq<Event>>>> next) {
        return next.get()
                .onSuccess(result -> eventStore.save(result.apply((r, e) -> e)
                        .map(e -> (Event) e)));
    }
}
