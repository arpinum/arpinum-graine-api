package yourapp.web.wallet;

import arpinum.command.CommandBus;
import arpinum.query.QueryBus;
import com.google.common.collect.ImmutableMap;
import yourapp.command.CreateWalletCommand;
import yourapp.query.wallet.FindAllWallets;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("/wallets")
public class WalletsResource {
    @Inject
    public WalletsResource(CommandBus bus, QueryBus queryBus) {
        this.bus = bus;
        this.queryBus = queryBus;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void get(@Suspended AsyncResponse response) {
        queryBus.send(new FindAllWallets())
                .onFailure(response::resume)
                .onSuccess(e -> response.resume(e.toJavaList()));
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
    private QueryBus queryBus;
}
