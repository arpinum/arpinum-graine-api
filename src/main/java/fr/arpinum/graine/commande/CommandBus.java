package fr.arpinum.graine.commande;


import com.google.common.util.concurrent.*;
import fr.arpinum.graine.infrastructure.bus.*;

import javax.inject.*;
import java.util.*;


@SuppressWarnings("UnusedDeclaration")
public class CommandBus extends AsynchronousBus {

    @Inject
    public CommandBus(Set<SynchronisationCommande> synchronisations, Set<CommandHandler> handlers) {
        super(synchronisations, handlers);
    }

    @Override
    public <TReponse> ListenableFuture<ExecutionResult<TReponse>> send(Message<TReponse> message) {
        return super.send(message);
    }
}


