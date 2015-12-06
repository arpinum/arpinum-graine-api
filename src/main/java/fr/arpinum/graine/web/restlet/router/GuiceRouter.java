package fr.arpinum.graine.web.restlet.router;

import com.google.inject.Injector;
import org.restlet.Context;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

@SuppressWarnings("UnusedDeclaration")
public abstract class GuiceRouter extends Router {
    public GuiceRouter(Context context, Injector injector) {
        super(context);
        this.injector = injector;
        route();
    }

    protected abstract void route();

    public GuiceRouter(Context context) {
        super(context);
    }

    @Override
    public Finder createFinder(Class<? extends ServerResource> resourceClass) {
        return new GuiceFinder(getContext(), resourceClass, injector);
    }

    protected Injector injector;
}
