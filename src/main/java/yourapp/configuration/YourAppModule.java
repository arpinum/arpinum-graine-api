package yourapp.configuration;


import arpinum.configuration.CqrsModule;
import arpinum.configuration.EventBusModule;
import arpinum.configuration.EventStoreModule;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import yourapp.infrastructure.repository.eventsource.RepositoriesEventSourced;
import yourapp.model.Repositories;

public class YourAppModule extends AbstractModule {
    @Override
    protected void configure() {
        binder().bind(Key.get(String.class, Names.named("boundedcontext.name"))).toInstance("yourapp");
        install(new EventBusModule("yourapp"));
        install(new EventStoreModule());
        install(new CqrsModule("yourapp"));
        install(new WebModule());
        bind(Repositories.class).to(RepositoriesEventSourced.class).in(Singleton.class);
        requestStaticInjection(Repositories.class);
    }
}
