package arpinum.infrastructure.bus.command;

import arpinum.command.*;
import arpinum.infrastructure.bus.*;
import io.reactivex.Single;

import javax.inject.Inject;
import java.util.Set;


@SuppressWarnings("UnusedDeclaration")
public class CommandBusAsynchronous extends AsynchronousBus implements CommandBus {

    @Inject
    public CommandBusAsynchronous(Set<SynchronisationCommande> synchronisations, Set<CommandHandler> handlers) {
        super(synchronisations, handlers);
    }

    @Override
    public <TReponse> Single<TReponse> send(Message<TReponse> message) {
        return super.dispatch(message)
                .firstOrError();
    }
}


