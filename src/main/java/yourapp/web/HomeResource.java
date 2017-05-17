package yourapp.web;


import arpinum.command.CommandBus;
import io.vavr.concurrent.Future;
import yourapp.command.CreateWalletCommand;

import javax.inject.Inject;
import java.util.UUID;

public class HomeResource {

    @Inject
    public HomeResource(CommandBus bus) {
        this.bus = bus;
    }


    public Future<UUID> handle() {
        return bus.send(new CreateWalletCommand("test"));
    }

    private CommandBus bus;
}
