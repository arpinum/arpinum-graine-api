package votreapplication;

import fr.arpinum.graine.web.restlet.Server;
import org.restlet.Context;
import votreapplication.web.VotreApplication;

import java.util.Optional;

public class Main {


    public static void main(String[] args) throws Exception {
        new Server(new VotreApplication(new Context())).start(port());
    }

    private static int port() {
        final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
        return Integer.parseInt(port.orElse("8080"));
    }
}
