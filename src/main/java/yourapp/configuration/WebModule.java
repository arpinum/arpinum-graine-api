package yourapp.configuration;


import arpinum.infrastructure.bus.guice.BusMagique;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import ratpack.handling.Handler;

public class WebModule extends AbstractModule {
    @Override
    protected void configure() {
        BusMagique.subtypesOf("yourapp.web", Handler.class)
                .forEach(r -> bind(r).in(Singleton.class));
    }
}
