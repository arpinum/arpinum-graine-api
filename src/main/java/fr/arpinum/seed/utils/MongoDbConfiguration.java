package fr.arpinum.seed.utils;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.mongodb.*;
import org.mongolink.*;

import javax.inject.*;
import java.net.*;
import java.util.*;


public class MongoDbConfiguration {
    public Settings settingsForMongoLink() {
        Settings settings = Settings.defaultInstance().withDefaultUpdateStrategy(UpdateStrategies.DIFF)
                .withAddresses(servers())
                .withDbName(name);
        if (withAuthentication()) {
            settings = settings.withAuthentication(user, password);
        }
        return settings;
    }

    public boolean withAuthentication() {
        return !Strings.isNullOrEmpty(user);
    }

    private List<ServerAddress> servers() {
        try {
            List<ServerAddress> servers = Lists.newArrayList();
            servers.add(new ServerAddress(resolveHost(host), port));
            if (!Strings.isNullOrEmpty(host2)) {
                servers.add(new ServerAddress(resolveHost(host2), port2));
            }
            return servers;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public String resolveHost(String value) {
        return value;
    }

    public DB dbForJongo() throws UnknownHostException {
        final MongoClient mongoClient = mongoClient();
        return mongoClient.getDB(name);
    }

    public MongoClient mongoClient() {
        List<MongoCredential> credentials = Lists.newArrayList();
        if (withAuthentication()) {
            credentials.add(MongoCredential.createMongoCRCredential(user, name, password.toCharArray()));
        }
        return new MongoClient(servers(), credentials);
    }

    @Inject
    @Named("db.name")
    public String name;
    @Inject
    @Named("db.user")
    public String user;
    @Inject
    @Named("db.password")
    public String password;
    @Inject
    @Named("db.port")
    public int port;
    @Inject
    @Named("db.port2")
    public int port2;
    @Inject
    @Named("db.host")
    private String host;
    @Inject
    @Named("db.host2")
    private String host2;

}
