package yourapp.command;


import arpinum.command.CommandHandler;
import yourapp.model.Repositories;
import yourapp.model.wallet.Wallet;

import java.util.UUID;

public class CreateWalletCommandHandler  implements CommandHandler<CreateWalletCommand, UUID>{

    @Override
    public UUID execute(CreateWalletCommand command) {
        Wallet wallet = Wallet.factory().create(command.name);
        Repositories.wallets().add(wallet);
        return wallet.getId();
    }
}
