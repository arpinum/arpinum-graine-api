package fr.arpinum.graine.web.restlet;

import fr.arpinum.graine.web.restlet.status.ApplicationStatusService;
import org.restlet.Application;
import org.restlet.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Handler;
import java.util.logging.LogManager;

@SuppressWarnings("UnusedDeclaration")
public class BaseApplication extends Application {

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

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseApplication.class);
}
