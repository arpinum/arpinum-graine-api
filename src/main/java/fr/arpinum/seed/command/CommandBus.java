package fr.arpinum.seed.command;

import com.google.common.util.concurrent.*;
import fr.arpinum.seed.infrastructure.bus.AsynchronousBus;
import fr.arpinum.seed.infrastructure.bus.ExecutionResult;
import fr.arpinum.seed.infrastructure.bus.Message;

import javax.inject.*;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
public class CommandBus extends AsynchronousBus {

    @Inject
    public CommandBus(Set<CommandSynchronization> synchronizations, Set<CommandHandler> handlers) {
        super(synchronizations, handlers);
    }

    @Override
    public <TReponse> ListenableFuture<ExecutionResult<TReponse>> send(Message<TReponse> message) {
        return super.send(message);
    }
}


