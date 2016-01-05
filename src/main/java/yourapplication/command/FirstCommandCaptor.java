package yourapplication.command;

import fr.arpinum.seed.command.CommandHandler;

@SuppressWarnings("UnusedDeclaration")
public class FirstCommandCaptor implements CommandHandler<FirstCommand, String> {

    @Override
    public String execute(FirstCommand firstCommand) {
        return null;
    }


}
