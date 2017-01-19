package yourapp.web;


import arpinum.command.CommandBus;
import io.reactivex.Single;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import yourapp.command.CreateWalletCommand;

import javax.inject.Inject;
import java.util.UUID;

public class HomeResource implements Handler {

    private CommandBus bus;

    @Inject
    public HomeResource(CommandBus bus) {
        this.bus = bus;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Promise.<UUID>async((p) -> {
            Single<UUID> result = bus.send(new CreateWalletCommand("test"));
            result.subscribe(p::success, p::error);
        }).then(n -> ctx.render(n.toString()));

    }
}
