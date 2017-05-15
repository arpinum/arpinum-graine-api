package yourapp.web;


import arpinum.command.CommandBus;
import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.Route;
import com.spotify.apollo.route.RouteProvider;
import io.vavr.concurrent.Future;
import yourapp.command.CreateWalletCommand;

import javax.inject.Inject;
import java.util.UUID;
import java.util.stream.Stream;

public class HomeResource implements RouteProvider {

    @Inject
    public HomeResource(CommandBus bus) {
        this.bus = bus;
    }

    @Override
    public Stream<? extends Route<? extends AsyncHandler<?>>> routes() {
        return Stream.of(
                Route.async("GET", "/", ctx -> handle().toCompletableFuture())
        );
    }

    public Future<UUID> handle() {
        return bus.send(new CreateWalletCommand("test"));
    }

    private CommandBus bus;
}
