package arpinum.boundedcontext;

import io.vavr.collection.List;
import org.jooby.Jooby;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BoundedContext {


    protected abstract List<Jooby.Module> modules();

    public abstract String name();


    private static final Logger LOGGER = LoggerFactory.getLogger(BoundedContext.class);

}
