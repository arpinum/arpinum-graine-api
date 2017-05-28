package arpinum.command;


import arpinum.ddd.event.Event;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

import java.util.function.Supplier;

public interface CommandMiddleware {

    Tuple2<?, Seq<Event<?>>> intercept(Command<?> message, Supplier< Tuple2<?, Seq<Event<?>>>> next);
}
