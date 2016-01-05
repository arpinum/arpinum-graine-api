package fr.arpinum.seed.web.restlet;

import fr.arpinum.seed.web.restlet.status.ApplicationStatusService;
import org.restlet.*;
import org.restlet.engine.application.CorsFilter;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.*;

@SuppressWarnings("UnusedDeclaration")
public abstract class BaseApplication extends Application {

    public BaseApplication(Context context) {
        super(context);
        // Désactivation des log JUL
        final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        final Handler[] handlers = rootLogger.getHandlers();
        for (final Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        setStatusService(new ApplicationStatusService());
        SLF4JBridgeHandler.install();
    }

    @Override
    public ApplicationStatusService getStatusService() {
        return (ApplicationStatusService) super.getStatusService();
    }

    @Override
    public synchronized void start() throws Exception {
        LOGGER.info("Démarrage de l'application");
        super.start();
    }

    @Override
    public synchronized void stop() throws Exception {
        LOGGER.info("Arrêt de l'application");
    }

    @Override
    public Restlet createInboundRoot() {
        CorsFilter filter = new CorsFilter(getContext(), routes());
        filter.setAllowedCredentials(true);
        filter.setAllowingAllRequestedHeaders(true);
        return filter;
    }

    protected abstract Router routes();

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseApplication.class);
}
