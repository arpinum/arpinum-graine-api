package arpinum.command;


import arpinum.infrastructure.Nothing;

public interface VoidCommandHandler<TCommand extends VoidCommand> extends CommandHandler<TCommand, Nothing> {

    @Override
    default Nothing execute(TCommand command) {
        doExecute(command);
        return Nothing.INSTANCE;
    }

    void doExecute(TCommand command);
}
