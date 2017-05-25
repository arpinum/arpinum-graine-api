package bin;


import arpinum.configuration.Cfg4jModule;
import arpinum.configuration.Configuration;
import arpinum.configuration.JongoModule;
import arpinum.configuration.MongoDbModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.LoggerFactory;
import yourapp.YourBoundedContext;

public class Main {

    public static void main(String[] args) throws Exception {
        UndertowJaxrsServer server = new UndertowJaxrsServer();
        Injector parentInjector = Guice.createInjector(stage()
                , new Cfg4jModule("app.yaml")
                , new MongoDbModule()
                , new JongoModule());
        server.deploy(new YourBoundedContext(parentInjector));
        Undertow.Builder serverConfiguration = Undertow.builder()
                .addHttpListener(8080, "localhost");
        server.start(serverConfiguration);
    }

    private static Stage stage() {
        return Configuration.stage("app", LoggerFactory.getLogger(Main.class));
    }


}