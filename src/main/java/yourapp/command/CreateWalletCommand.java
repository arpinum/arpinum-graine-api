package yourapp.command;


import arpinum.command.Command;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

public class CreateWalletCommand implements Command<UUID> {

    @NotEmpty
    public String name;
}
