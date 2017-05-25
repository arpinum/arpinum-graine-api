package yourapp;

import arpinum.infrastructure.bus.guice.ScanMagique;
import com.google.inject.Injector;
import yourapp.configuration.YourAppModule;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationPath("/bc")
public class YourBoundedContext extends Application {


    private final Injector injector;

    public YourBoundedContext(Injector parentInjector) {
        injector = parentInjector.createChildInjector(new YourAppModule());
    }

    @Override
    public Set<Object> getSingletons() {
        return ScanMagique.searchForAnnotatedClass("yourapp.web", Path.class)
                .stream().map(injector::getInstance)
                .collect(Collectors.toSet());
    }
}
