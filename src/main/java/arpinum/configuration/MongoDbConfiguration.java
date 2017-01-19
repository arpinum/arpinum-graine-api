package arpinum.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class MongoDbConfiguration {

    public MongoClient mongoClient() {
        return new MongoClient(getUri());
    }

    public MongoClientURI getUri() {
        return new MongoClientURI(uri);
    }

    private String uri;
}
