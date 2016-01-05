package fr.arpinum.seed.boundedcontext;

import com.google.inject.*;
import fr.arpinum.seed.utils.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BoundedContext {

    protected void createInjector() {
        injector = Guice.createInjector(Configuration.stage(name(), LOGGER), module());
    }

    protected abstract String name();

    protected abstract List<AbstractModule> module();

    private static final Logger LOGGER = LoggerFactory.getLogger(BoundedContext.class);
    protected Injector injector;
}
