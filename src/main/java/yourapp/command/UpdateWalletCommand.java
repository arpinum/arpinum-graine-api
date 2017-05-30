package yourapp.command;


import arpinum.command.VoidCommand;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class UpdateWalletCommand implements VoidCommand {

    @NotNull
    public UUID id;
    @NotEmpty
    public String name;

    public UpdateWalletCommand withId(UUID id) {
        this.id = id;
        return this;
    }
}
