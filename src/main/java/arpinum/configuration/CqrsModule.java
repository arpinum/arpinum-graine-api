package arpinum.configuration;

import arpinum.infrastructure.persistance.eventsourcing.UnitOfWork;
import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import arpinum.command.CommandBus;
import arpinum.command.CommandHandler;
import arpinum.command.CommandValidator;
import arpinum.command.SynchronisationCommande;
import arpinum.infrastructure.bus.command.CommandBusAsynchronous;
import arpinum.infrastructure.bus.event.CommandSynchronizedEventBus;
import arpinum.infrastructure.bus.guice.BusMagique;
import arpinum.query.QueryBus;
import arpinum.query.QueryHandler;
import arpinum.query.QuerySynchronization;

import javax.validation.*;

public class CqrsModule extends AbstractModule {

    public CqrsModule(String packageName) {
        this.commandPackage = packageName + ".command";
        this.queryPackage = packageName + ".query";
    }

    @Override
    protected void configure() {
        configureCommands();
        configureQuery();
    }

    private void configureCommands() {
        final Multibinder<SynchronisationCommande> multibinder = Multibinder.newSetBinder(binder(), SynchronisationCommande.class);
        multibinder.addBinding().to(CommandValidator.class);
        multibinder.addBinding().to(UnitOfWork.class);
        multibinder.addBinding().to(CommandSynchronizedEventBus.class);
        BusMagique.scanPackageAndBind(commandPackage, CommandHandler.class, binder());
        bind(CommandBus.class).to(CommandBusAsynchronous.class).asEagerSingleton();
    }

    private void configureQuery() {
        final Multibinder<QuerySynchronization> multibinder = Multibinder.newSetBinder(binder(), QuerySynchronization.class);
        BusMagique.scanPackageAndBind(queryPackage, QueryHandler.class, binder());
        bind(QueryBus.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    private final String commandPackage;
    private final String queryPackage;
}
