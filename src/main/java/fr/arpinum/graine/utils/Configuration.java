package fr.arpinum.graine.utils;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.inject.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public final class Configuration {

    public static Stage stage(String context, Logger logger) {
        final String env = Environment.get();
        logger.info("Configuration de " + context + " en mode {}", env);
        if ("dev".equals(env)) {
            return Stage.DEVELOPMENT;
        }
        return Stage.PRODUCTION;
    }

    public static Properties properties(final String name) {
        URL url = Resources.getResource("env/" + Environment.get() + "/" + name + ".properties");
        ByteSource inputSupplier = Resources
                .asByteSource(url);
        Properties propriétés = new Properties();
        try {
            propriétés.load(inputSupplier.openStream());
        } catch (IOException e) {
            LOGGER.error("Impossible de charger la configuration", e);
        }
        return propriétés;
    }


    private Configuration() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
}
