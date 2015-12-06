package votreapplication.web.configuration;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import fr.arpinum.graine.commande.CommandBus;
import fr.arpinum.graine.commande.CommandHandler;
import fr.arpinum.graine.commande.CommandValidator;
import fr.arpinum.graine.commande.SynchronisationCommande;
import fr.arpinum.graine.infrastructure.bus.guice.BusMagique;
import fr.arpinum.graine.infrastructure.persistance.mongo.MongoLinkContext;
import fr.arpinum.graine.modele.evenement.AsyncEventBus;
import fr.arpinum.graine.modele.evenement.EventBus;
import fr.arpinum.graine.modele.evenement.EventCaptor;
import fr.arpinum.graine.modele.evenement.SynchronisationEvenement;
import fr.arpinum.graine.recherche.ResearchBus;
import fr.arpinum.graine.recherche.ResearchHandler;
import org.jongo.Jongo;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;
import org.mongolink.domain.mapper.ContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import votreapplication.infrastructure.persistance.mongo.LocalisateurEntrepotsMongoLink;
import votreapplication.modele.LocalisateurEntrepots;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class ConfigurationGuice extends AbstractModule {
    @Override
    protected void configure() {
        Names.bindProperties(binder(), propriétés());
        configurePersistance();
        configureEvements();
        configureCommandes();
        configureRecherches();
        requestStaticInjection(LocalisateurEntrepots.class);

    }

    private Properties propriétés() {
        URL url = Resources.getResource("env/" + Optional.ofNullable(System.getenv("env")).orElse("dev") + "/application.properties");
        ByteSource inputSupplier = Resources
                .asByteSource(url);
        Properties propriétés = new Properties();
        try {
            propriétés.load(inputSupplier.openStream());
        } catch (IOException e) {
            LOGGER.error("Impossible de charger la configuration", e);
        }
        return propriétés;
    }

    private void configurePersistance() {
        bind(MongoLinkContext.class).in(Singleton.class);

        bind(LocalisateurEntrepots.class).to(LocalisateurEntrepotsMongoLink.class).in(Singleton.class);
    }

    private void configureRecherches() {
        BusMagique.scanPackageEtBind("votreapplication.recherche", ResearchHandler.class, binder());
        bind(ResearchBus.class).asEagerSingleton();
    }

    private void configureCommandes() {
        final Multibinder<SynchronisationCommande> multibinder = Multibinder.newSetBinder(binder(), SynchronisationCommande.class);
        multibinder.addBinding().to(MongoLinkContext.class);
        multibinder.addBinding().to(CommandValidator.class);
        multibinder.addBinding().to(AsyncEventBus.class);
        BusMagique.scanPackageEtBind("votreapplication.commande", CommandHandler.class, binder());
        bind(CommandBus.class).asEagerSingleton();
    }

    private void configureEvements() {
        final Multibinder<SynchronisationEvenement> multibinder = Multibinder.newSetBinder(binder(), SynchronisationEvenement.class);
        multibinder.addBinding().to(MongoLinkContext.class);
        BusMagique.scanPackageEtBind("votreapplication", EventCaptor.class, binder());
        bind(EventBus.class).to(AsyncEventBus.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    AsyncEventBus busEvenementAsynchrone(Set<SynchronisationEvenement> synchronisationEvenements, Set<EventCaptor> evenements) {
        return new AsyncEventBus(synchronisationEvenements, evenements);
    }

    @Provides
    @Singleton
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Provides
    @Singleton
    public MongoSessionManager mongoLink(ConfigurationMongoDb configurationMongoDb) {
        Settings settings = Settings.defaultInstance().withDefaultUpdateStrategy(UpdateStrategies.DIFF)
                .withDbName(configurationMongoDb.name)
                .withHost(configurationMongoDb.host)
                .withPort(configurationMongoDb.port);
        if (configurationMongoDb.avecAuthentificationDB()) {
            settings = settings.withAuthentication(configurationMongoDb.user, configurationMongoDb.password);
        }

        return MongoSessionManager.create(new ContextBuilder("votreapplication.infrastructure.persistance.mongo.mapping"),
                settings);
    }

    @Provides
    @Singleton
    public Jongo jongo(ConfigurationMongoDb configurationMongoDb) throws UnknownHostException {
        final MongoClient mongoClient = new MongoClient(configurationMongoDb.host, configurationMongoDb.port);
        final DB db = mongoClient.getDB(configurationMongoDb.name);
        if (configurationMongoDb.avecAuthentificationDB()) {
            db.authenticate(configurationMongoDb.user, configurationMongoDb.password.toCharArray());
        }
        return new Jongo(db);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationGuice.class);
}
