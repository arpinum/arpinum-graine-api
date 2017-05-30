package yourapp.command;


import arpinum.command.VoidCommandHandler;
import arpinum.ddd.event.Event;
import io.vavr.collection.Seq;
import yourapp.model.Repositories;

public class UpdateWalletCommandHandler implements VoidCommandHandler<UpdateWalletCommand> {

    @Override
    public Seq<Event> doExecute(UpdateWalletCommand command) {
        return Repositories.wallets().get(command.id)
                .map(w -> (Event) w.changeName(command.name))
                .toList();
    }
}
