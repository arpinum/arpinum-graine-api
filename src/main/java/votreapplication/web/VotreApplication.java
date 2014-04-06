package votreapplication.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import fr.arpinum.graine.web.restlet.BaseApplication;
import fr.arpinum.graine.web.restlet.router.GuiceRouter;
import org.restlet.Context;
import votreapplication.web.configuration.VotreApplicationModule;
import votreapplication.web.ressource.IndexRessource;

import java.util.Optional;

public class VotreApplication extends BaseApplication {

    public VotreApplication(Context context) {
        super(context);
    }

    @Override
    protected GuiceRouter routes() {
        return new GuiceRouter(getContext(), injector()) {
            @Override
            protected void route() {
                attachDefault(IndexRessource.class);
            }
        };
    }

    private Injector injector() {
        return Guice.createInjector(stage(), new VotreApplicationModule());
    }

    private Stage stage() {
        final Optional<String> env = Optional.ofNullable(System.getenv("env"));
        LOGGER.info("Configuration en mode {}", env.orElse("dev"));
        if (env.orElse("dev").equals("dev")) {
            return Stage.DEVELOPMENT;
        }
        return Stage.PRODUCTION;
    }

}
