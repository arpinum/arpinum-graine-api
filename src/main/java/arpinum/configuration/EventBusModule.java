package arpinum.configuration;

import arpinum.infrastructure.bus.event.AsyncEventBus;
import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import arpinum.ddd.evenement.EventBus;
import arpinum.ddd.evenement.EventCaptor;
import arpinum.ddd.evenement.EventBusMiddleware;
import arpinum.infrastructure.bus.event.CommandSynchronizedEventBus;
import arpinum.infrastructure.bus.guice.ScanMagique;

import java.util.Set;

public class EventBusModule extends AbstractModule {

    public EventBusModule(String packageName) {
        this.packageName = packageName;
    }

    @Override
    protected void configure() {
        configureEventBus();

    }
    private void configureEventBus() {
        final Multibinder<EventBusMiddleware> multibinder = Multibinder.newSetBinder(binder(), EventBusMiddleware.class);
        ScanMagique.scanPackageAndBind(packageName, EventCaptor.class, binder());
        bind(EventBus.class).to(CommandSynchronizedEventBus.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public CommandSynchronizedEventBus asyncEventBus(Set<EventBusMiddleware> synchronisationEvenements, Set<EventCaptor> evenements) {
        return new AsyncEventBus(synchronisationEvenements, evenements);
    }

    private String packageName;
}
