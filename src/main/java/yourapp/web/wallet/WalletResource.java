package yourapp.web.wallet;

import arpinum.command.CommandBus;
import yourapp.command.UpdateWalletCommand;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/wallets/{id}")
public class WalletResource {

    @Inject
    public WalletResource(CommandBus bus) {
        this.bus = bus;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id")UUID id, UpdateWalletCommand command) {
        bus.send(command.withId(id));
    }

    private CommandBus bus;
}
