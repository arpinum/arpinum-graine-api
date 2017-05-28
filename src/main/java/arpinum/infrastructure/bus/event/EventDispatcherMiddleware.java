package arpinum.infrastructure.bus.event;


import arpinum.command.Command;
import arpinum.command.CommandMiddleware;
import arpinum.ddd.event.Event;
import arpinum.ddd.event.EventBus;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

import javax.inject.Inject;
import java.util.function.Supplier;

public class EventDispatcherMiddleware implements CommandMiddleware {

    @Inject
    public EventDispatcherMiddleware(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public Tuple2<?, Seq<Event<?>>> intercept(Command<?> message, Supplier<Tuple2<?, Seq<Event<?>>>> next) {
        final Tuple2<?, Seq<Event<?>>> result = next.get();
        bus.publish(result.apply((r, e) -> e));
        return result;
    }

    private EventBus bus;
}
