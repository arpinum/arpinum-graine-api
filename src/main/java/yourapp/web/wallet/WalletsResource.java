package yourapp.web.wallet;

import arpinum.command.CommandBus;
import com.google.common.collect.ImmutableMap;
import yourapp.command.CreateWalletCommand;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("/wallets")
public class WalletsResource {
    @Inject
    public WalletsResource(CommandBus bus) {
        this.bus = bus;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(@Suspended AsyncResponse response, CreateWalletCommand command) {
        bus.send(command)
                .map(i -> ImmutableMap.of("id", i.toString()))
                .onFailure(response::resume)
                .onSuccess(response::resume);
    }

    private final CommandBus bus;
}
