package arpinum.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import arpinum.infrastructure.persistance.JongoBuilder;
import arpinum.infrastructure.persistance.JongoProvider;

import java.net.UnknownHostException;

public class JongoModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public Jongo jongo(MongoDbConfiguration mongoDbConfiguration, MongoClient client) throws UnknownHostException {
        return JongoBuilder.build(client.getDB(mongoDbConfiguration.getUri().getDatabase()));
    }

    @Provides
    @Singleton
    public JongoProvider jongoProvider(Jongo jongo) {
        return () -> jongo;
    }
}
