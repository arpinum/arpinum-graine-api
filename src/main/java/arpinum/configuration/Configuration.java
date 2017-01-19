package arpinum.configuration;

import com.google.inject.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Configuration {

    public static Stage stage(String context, Logger logger) {
        final String env = Environment.get();
        logger.info("Configuration de " + context + " en mode {}", env);
        if ("dev".equals(env)) {
            return Stage.DEVELOPMENT;
        }
        return Stage.PRODUCTION;
    }


    private Configuration() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
}
