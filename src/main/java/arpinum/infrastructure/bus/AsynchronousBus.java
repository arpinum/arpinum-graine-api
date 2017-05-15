package arpinum.infrastructure.bus;

import io.vavr.NotImplementedError;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class AsynchronousBus implements Bus {

    public AsynchronousBus(Seq<BusMiddleware> middlewares, Seq<? extends MessageCaptor> handlers, ExecutorService executor) {
        this.handlers = handlers.groupBy(h -> (Class<?>) h.commandType());
    }

    @Override
    public <TReponse> Future<TReponse> dispatch(Message<TReponse> message) {
        return Future.failed(new NotImplementedError());
    }


    protected final static Logger LOGGER = LoggerFactory.getLogger(AsynchronousBus.class);

    protected final Map<Class<?>, ? extends Seq<? extends MessageCaptor>> handlers;

}
