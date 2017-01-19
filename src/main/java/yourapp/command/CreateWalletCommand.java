package yourapp.command;


import arpinum.command.Command;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

public class CreateWalletCommand implements Command<UUID> {

    public CreateWalletCommand(String name) {
        this.name = name;
    }

    @NotEmpty
    public final String name;
}
