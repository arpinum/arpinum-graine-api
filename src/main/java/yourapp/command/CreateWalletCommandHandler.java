package yourapp.command;


import arpinum.command.CommandHandler;
import arpinum.ddd.event.Event;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import yourapp.model.wallet.Wallet;
import yourapp.model.wallet.WalletCreated;

import java.util.UUID;

public class CreateWalletCommandHandler  implements CommandHandler<CreateWalletCommand, UUID>{


    @Override
    public Tuple2<UUID, Seq<Event<?>>> execute(CreateWalletCommand createWalletCommand) {
        Tuple2<Wallet, WalletCreated> wallet = Wallet.factory().create(createWalletCommand.name);
        return wallet.map((w, e)-> Tuple.of(w.getId(), List.of(e)));
    }
}
