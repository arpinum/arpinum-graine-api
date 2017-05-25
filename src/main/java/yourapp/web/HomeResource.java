package yourapp.web;


import arpinum.command.CommandBus;
import io.vavr.concurrent.Future;
import yourapp.command.CreateWalletCommand;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("/")
public class HomeResource {

    @Inject
    public HomeResource(CommandBus bus) {
        this.bus = bus;
    }


    @GET
    public String hello() {
        return "fello";
    }

    private CommandBus bus;
}
