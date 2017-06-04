package yourapp.saga;


import arpinum.command.CommandBus;
import arpinum.saga.SagaHandler;
import io.vavr.concurrent.Future;
import yourapp.command.CreateWalletCommand;
import yourapp.command.UpdateWalletCommand;

public class SagaTestHandler implements SagaHandler<String, SagaTest> {

    @Override
    public Future<String> run(CommandBus bus, SagaTest saga) {
        CreateWalletCommand message = new CreateWalletCommand();
        message.name = saga.name;
        return bus.send(message)
                .flatMap(id -> {
                    UpdateWalletCommand update = new UpdateWalletCommand();
                    update.id = id;
                    update.name = saga.name + "_updated";
                    return bus.send(update)
                            .map(r -> id);
                })
                .map(r -> r.toString());
    }
}
