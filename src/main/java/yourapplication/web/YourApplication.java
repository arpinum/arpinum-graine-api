package yourapplication.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import fr.arpinum.seed.web.restlet.BaseApplication;
import fr.arpinum.seed.web.restlet.router.GuiceRouter;
import org.restlet.Context;
import yourapplication.model.RepositoriesLocator;
import yourapplication.web.configuration.GuiceConfiguration;
import yourapplication.web.ressource.IndexRessource;

import java.util.Optional;

public class YourApplication extends BaseApplication {

    public YourApplication(Context context) {
        super(context);
        injector = Guice.createInjector(stage(), new GuiceConfiguration());
        RepositoriesLocator.initialise(injector.getInstance(RepositoriesLocator.class));
    }

    private Stage stage() {
        final Optional<String> env = Optional.ofNullable(System.getenv("env"));
        LOGGER.info("Configuration in mode {}", env.orElse("dev"));
        if (env.orElse("dev").equals("dev")) {
            return Stage.DEVELOPMENT;
        }
        return Stage.PRODUCTION;
    }


    @Override
    protected GuiceRouter routes() {
        return new GuiceRouter(getContext(), injector) {
            @Override
            protected void route() {
                attachDefault(IndexRessource.class);
            }
        };
    }

    private final Injector injector;
}
