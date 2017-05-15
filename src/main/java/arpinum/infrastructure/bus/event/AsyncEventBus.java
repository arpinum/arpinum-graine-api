package arpinum.infrastructure.bus.event;

import arpinum.command.Command;
import arpinum.ddd.evenement.Event;
import arpinum.ddd.evenement.EventCaptor;
import arpinum.ddd.evenement.EventBusMiddleware;
import arpinum.infrastructure.bus.AsynchronousBus;
import arpinum.infrastructure.bus.BusMiddleware;
import arpinum.infrastructure.bus.Message;
import com.google.common.collect.Lists;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Try;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("UnusedDeclaration")
public class AsyncEventBus extends AsynchronousBus implements CommandSynchronizedEventBus {

    @Inject
    public AsyncEventBus(Set<EventBusMiddleware> synchronisations, Set<EventCaptor> handlers) {
        super(null, null, null);
    }


    @Override
    public void publish(Event[] events) {

    }

    @Override
    public <T> Try<T> handle(Message<T> message, BusMiddleware next) {
        return null;
    }

    @Override
    public Tuple2<?, Seq<Event<?>>> intercept(Command<?> message, Supplier<Tuple2<?, Seq<Event<?>>>> next) {
        return null;
    }
}
