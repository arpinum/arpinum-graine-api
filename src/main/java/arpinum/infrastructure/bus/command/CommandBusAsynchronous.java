package arpinum.infrastructure.bus.command;

import arpinum.command.Command;
import arpinum.command.CommandBus;
import arpinum.command.CommandHandler;
import arpinum.command.CommandMiddleware;
import arpinum.ddd.event.Event;
import arpinum.infrastructure.bus.CaptorNotFound;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ExecutorService;


@SuppressWarnings("UnusedDeclaration")
public class CommandBusAsynchronous implements CommandBus {

    @Inject
    public CommandBusAsynchronous(Set<CommandMiddleware> middlewares, Set<CommandHandler> handlers, ExecutorService executor) {
        this.executor = executor;
        this.handlers = List.ofAll(handlers);
        middlewareChain = List.ofAll(middlewares).foldRight(new HandlerInvokation(), Chain::new);
    }

    @Override
    public <TReponse> Future<TReponse> send(Command<TReponse> message) {
        return handlers.find(h -> h.commandType().equals(message.getClass()))
                .map(h -> (CommandHandler<Command<TReponse>, TReponse>) h)
                .map(h -> execute(h, message))
                .getOrElse(() -> Future.failed(new CaptorNotFound(message.getClass())));
    }

    private <TReponse> Future<TReponse> execute(CommandHandler<Command<TReponse>, TReponse> handler, Command<TReponse> command) {
        return Future.of(executor, () -> middlewareChain
                .apply(handler, command)
                .apply((r, e) -> r));
    }


    private final Seq<CommandHandler> handlers;

    private final Chain middlewareChain;
    private final ExecutorService executor;
    private final static Logger LOGGER = LoggerFactory.getLogger(CommandBusAsynchronous.class);

    private static class Chain {

        Chain(CommandMiddleware current, Chain next) {
            this.current = current;
            this.next = next;
        }

        public <T> Tuple2<T, Seq<Event<?>>> apply(CommandHandler<Command<T>, T> h, Command<T> command) {
            LOGGER.debug("Running middleware {}", current.getClass());
            return (Tuple2<T, Seq<Event<?>>>) current.intercept(command, () -> next.apply(h, command));
        }

        private CommandMiddleware current;
        private Chain next;
    }

    private static class HandlerInvokation extends Chain {
        public HandlerInvokation() {
            super(null, null);
        }

        @Override
        public <T> Tuple2<T, Seq<Event<?>>> apply(CommandHandler<Command<T>, T> h, Command<T> command) {
            LOGGER.debug("Applying handler {}", h.getClass());
            return h.execute(command);
        }
    }
}


