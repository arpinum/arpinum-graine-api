package yourapp;

import com.spotify.apollo.Environment;
import com.spotify.apollo.module.AbstractApolloModule;
import com.spotify.apollo.route.Route;


public class YourBoundedContext extends AbstractApolloModule {

    @Override
    protected void configure() {

    }

    @Override
    public String getId() {
        return "yourboundedcontext";
    }

    public static void start(Environment environment) {
        environment.routingEngine().registerAutoRoute(Route.sync("GET", "/ping", (ctx)->"pong"));
    }

}
