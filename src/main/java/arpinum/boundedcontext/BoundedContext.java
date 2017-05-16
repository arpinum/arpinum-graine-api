package arpinum.boundedcontext;

import com.spotify.apollo.Environment;
import com.spotify.apollo.core.Service;
import com.spotify.apollo.core.Services;
import com.spotify.apollo.module.ApolloModule;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public abstract class BoundedContext  {

    public BoundedContext() {
        configure();
    }

    private void configure() {
        service = modules().foldLeft(Services.usingName(name()), (a, b) -> a.withModule(b))
                .build();
    }

    public void start(Environment.RoutingEngine routingEngine)  {
        LOGGER.info("Starting {}", name());
        registerRoutes(routingEngine);
        try {
            service.start();
        } catch (IOException e) {
            LOGGER.error("Error running bounded context", e);
        }
    }

    protected abstract void registerRoutes(Environment.RoutingEngine routingEngine);

    protected abstract List<ApolloModule> modules();

    public abstract String name();

    public Service service() {
        return service;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BoundedContext.class);

    private Service service;
}
