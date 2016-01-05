package yourapplication.web.configuration;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import fr.arpinum.seed.command.CommandBus;
import fr.arpinum.seed.command.CommandHandler;
import fr.arpinum.seed.command.CommandValidator;
import fr.arpinum.seed.command.CommandSynchronization;
import fr.arpinum.seed.infrastructure.bus.guice.MagicalBus;
import fr.arpinum.seed.infrastructure.persistence.mongo.MongoLinkContext;
import fr.arpinum.seed.model.event.AsyncEventBus;
import fr.arpinum.seed.model.event.EventBus;
import fr.arpinum.seed.model.event.EventCaptor;
import fr.arpinum.seed.model.event.EventSynchronization;
import fr.arpinum.seed.query.QueryBus;
import fr.arpinum.seed.query.QueryHandler;
import fr.arpinum.seed.query.QuerySynchronization;
import org.jongo.Jongo;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;
import org.mongolink.domain.mapper.ContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yourapplication.infrastructure.persistence.mongo.MongoLinkRepositoryLocator;
import yourapplication.model.RepositoriesLocator;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class GuiceConfiguration extends AbstractModule {
    @Override
    protected void configure() {
        Names.bindProperties(binder(), properties());
        configurePersistence();
        configureEvents();
        configureCommands();
        configureQueries();
        requestStaticInjection(RepositoriesLocator.class);

    }

    private Properties properties() {
        URL url = Resources.getResource("env/" + Optional.ofNullable(System.getenv("env")).orElse("dev") + "/application.properties");
        ByteSource inputSupplier = Resources
                .asByteSource(url);
        Properties properties = new Properties();
        try {
            properties.load(inputSupplier.openStream());
        } catch (IOException e) {
            LOGGER.error("Impossible to load the configuration", e);
        }
        return properties;
    }

    private void configurePersistence() {
        bind(MongoLinkContext.class).in(Singleton.class);

        bind(RepositoriesLocator.class).to(MongoLinkRepositoryLocator.class).in(Singleton.class);
    }

    private void configureQueries() {
        final Multibinder<QuerySynchronization> multibinder = Multibinder.newSetBinder(binder(), QuerySynchronization.class);
        MagicalBus.scanPackageAndBind("yourapplication.query", QueryHandler.class, binder());
        bind(QueryBus.class).asEagerSingleton();
    }

    private void configureCommands() {
        final Multibinder<CommandSynchronization> multibinder = Multibinder.newSetBinder(binder(), CommandSynchronization.class);
        multibinder.addBinding().to(MongoLinkContext.class);
        multibinder.addBinding().to(CommandValidator.class);
        multibinder.addBinding().to(AsyncEventBus.class);
        MagicalBus.scanPackageAndBind("yourapplication.command", CommandHandler.class, binder());
        bind(CommandBus.class).asEagerSingleton();
    }

    private void configureEvents() {
        final Multibinder<EventSynchronization> multibinder = Multibinder.newSetBinder(binder(), EventSynchronization.class);
        multibinder.addBinding().to(MongoLinkContext.class);
        MagicalBus.scanPackageAndBind("votreapplication", EventCaptor.class, binder());
        bind(EventBus.class).to(AsyncEventBus.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    AsyncEventBus asyncEventBus(Set<EventSynchronization> eventSynchronizations, Set<EventCaptor> events) {
        return new AsyncEventBus(eventSynchronizations, events);
    }

    @Provides
    @Singleton
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Provides
    @Singleton
    public MongoSessionManager mongoLink(MongoDbConfiguration mongoDbConfiguration) {
        Settings settings = Settings.defaultInstance().withDefaultUpdateStrategy(UpdateStrategies.DIFF)
                .withDbName(mongoDbConfiguration.name)
                .withHost(mongoDbConfiguration.host)
                .withPort(mongoDbConfiguration.port);
        if (mongoDbConfiguration.withDBAuthentication()) {
            settings = settings.withAuthentication(mongoDbConfiguration.user, mongoDbConfiguration.password);
        }

        return MongoSessionManager.create(new ContextBuilder("yourapplication.infrastructure.persistence.mongo.mapping"),
                settings);
    }

    @Provides
    @Singleton
    public Jongo jongo(MongoDbConfiguration mongoDbConfiguration) throws UnknownHostException {
        final MongoClient mongoClient = new MongoClient(mongoDbConfiguration.host, mongoDbConfiguration.port);
        final DB db = mongoClient.getDB(mongoDbConfiguration.name);
        if (mongoDbConfiguration.withDBAuthentication()) {
            db.authenticate(mongoDbConfiguration.user, mongoDbConfiguration.password.toCharArray());
        }
        return new Jongo(db);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceConfiguration.class);
}
