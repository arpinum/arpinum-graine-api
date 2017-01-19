package arpinum.boundedcontext;

import com.google.inject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import arpinum.configuration.Configuration;

import java.util.List;

public abstract class BoundedContext {

    protected void createInjector(Injector parentInjector) {
        Stage stage = Configuration.stage(name(), LOGGER);
        injector = parentInjector.createChildInjector(module(stage));
    }

    protected abstract String name();

    protected abstract List<AbstractModule> module(Stage stage);

    private static final Logger LOGGER = LoggerFactory.getLogger(BoundedContext.class);
    protected Injector injector;
}
