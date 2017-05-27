package yourapp.command;


import arpinum.command.CommandHandler;
import arpinum.ddd.evenement.Event;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import yourapp.model.Repositories;
import yourapp.model.wallet.Wallet;

import java.util.UUID;

public class CreateWalletCommandHandler  implements CommandHandler<CreateWalletCommand, UUID>{


    @Override
    public Tuple2<UUID, Seq<Event<?>>> execute(CreateWalletCommand createWalletCommand) {
        Wallet wallet = Wallet.factory().create(createWalletCommand.name);
        return Tuple.of(wallet.getId(), List.empty());
    }
}
