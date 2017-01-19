package arpinum.configuration;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import ratpack.guice.ConfigurableModule;

public class MongoDbModule extends ConfigurableModule<MongoDbConfiguration> {
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public MongoClient client(MongoDbConfiguration configuration) {
        return configuration.mongoClient();
    }
}
