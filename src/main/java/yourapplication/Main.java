package yourapplication;

import fr.arpinum.seed.web.restlet.Server;
import org.restlet.Context;
import yourapplication.web.YourApplication;

import java.util.Optional;

public class Main {


    public static void main(String[] args) throws Exception {
        new Server(new YourApplication(new Context())).start(port());
    }

    private static int port() {
        final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
        return Integer.parseInt(port.orElse("8080"));
    }
}
