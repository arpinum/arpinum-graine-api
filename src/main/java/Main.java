import arpinum.configuration.JongoModule;
import arpinum.configuration.MongoDbConfiguration;
import arpinum.configuration.MongoDbModule;
import ratpack.func.Action;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import yourapp.configuration.YourAppModule;
import yourapp.web.HomeResource;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        ServerConfig config = ServerConfig.builder()
                .yaml(Main.class.getClassLoader().getResource("application.yaml"))
                .onError(Action.noop())
                .yaml(Paths.get("configuration", "application.yaml"))
                .env("APP")
                .require("/mongo", MongoDbConfiguration.class)
                .build();
        RatpackServer.start(server -> server
                .serverConfig(config)
                .registry(Guice.registry(s ->
                        s.moduleConfig(MongoDbModule.class, config.get(MongoDbConfiguration.class))
                                .module(JongoModule.class)
                                .module(YourAppModule.class)
                ))
                .handlers(chain ->
                        chain
                                .path(HomeResource.class)

                ));
    }
}
