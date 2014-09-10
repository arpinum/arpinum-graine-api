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
import fr.arpinum.graine.commande.BusCommande;
import fr.arpinum.graine.commande.CapteurCommande;
import fr.arpinum.graine.commande.SynchronisationCommande;
import fr.arpinum.graine.commande.ValidateurCommande;
import fr.arpinum.graine.infrastructure.bus.guice.BusMagique;
import fr.arpinum.graine.infrastructure.persistance.mongo.ContexteMongoLink;
import fr.arpinum.graine.modele.evenement.BusEvenement;
import fr.arpinum.graine.modele.evenement.BusEvenementAsynchrone;
import fr.arpinum.graine.modele.evenement.CapteurEvenement;
import fr.arpinum.graine.modele.evenement.SynchronisationEvenement;
import fr.arpinum.graine.recherche.BusRecherche;
import fr.arpinum.graine.recherche.CapteurRecherche;
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
        bind(ContexteMongoLink.class).in(Singleton.class);

        bind(LocalisateurEntrepots.class).to(LocalisateurEntrepotsMongoLink.class).in(Singleton.class);
    }

    private void configureRecherches() {
        BusMagique.scanPackageEtBind("votreapplication.recherche", CapteurRecherche.class, binder());
        bind(BusRecherche.class).asEagerSingleton();
    }

    private void configureCommandes() {
        final Multibinder<SynchronisationCommande> multibinder = Multibinder.newSetBinder(binder(), SynchronisationCommande.class);
        multibinder.addBinding().to(ValidateurCommande.class);
        multibinder.addBinding().to(ContexteMongoLink.class);
        multibinder.addBinding().to(BusEvenementAsynchrone.class);
        BusMagique.scanPackageEtBind("votreapplication.commande", CapteurCommande.class, binder());
        bind(BusCommande.class).asEagerSingleton();
    }

    private void configureEvements() {
        final Multibinder<SynchronisationEvenement> multibinder = Multibinder.newSetBinder(binder(), SynchronisationEvenement.class);
        multibinder.addBinding().to(ContexteMongoLink.class);
        BusMagique.scanPackageEtBind("votreapplication", CapteurEvenement.class, binder());
        bind(BusEvenement.class).to(BusEvenementAsynchrone.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    BusEvenementAsynchrone busEvenementAsynchrone(Set<SynchronisationEvenement> synchronisationEvenements, Set<CapteurEvenement> evenements) {
        return new BusEvenementAsynchrone(synchronisationEvenements, evenements);
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
