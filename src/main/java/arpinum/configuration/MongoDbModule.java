package arpinum.configuration;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;

public class MongoDbModule {


    @Provides
    @Singleton
    public MongoClient client(MongoDbConfiguration configuration) {
        return configuration.mongoClient();
    }
}
