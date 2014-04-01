package fr.arpinum.graine.commande

import com.google.common.collect.Sets
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import fr.arpinum.graine.bus.Commande
import fr.arpinum.graine.bus.HandlerCommande
import fr.arpinum.graine.bus.ResultatCommande
import fr.arpinum.graine.bus.SynchronisationBus
import spock.lang.Specification

import java.util.concurrent.ExecutorService

import static org.mockito.Mockito.*

public class BusCommandeAsynchroneTest extends Specification {


    def "peut exécuter une commande"() {
        given:
        def handler = unHandler()
        def bus = busAvec(handler)
        def commande = new FausseCommande()

        when:
        bus.poste(commande)

        then:
        handler.commandeReçue == commande
    }

    def "est asynchrone"() {
        given:
        def executor = Mock(ExecutorService.class)
        def bus = bus()
        bus.setExecutor(executor)

        when:
        bus.poste(new FausseCommande())

        then:
        1 * executor.execute(!null)
    }


    def "encapsule les commandes dans les synchronisations"() {
        given:
        def synchro = Mock(SynchronisationBus)
        def bus = busAvec(synchro)
        def commande = new FausseCommande()

        when:
        bus.poste(commande)

        then:
        1 * synchro.avantExecution(commande)
        then:
        1 * synchro.apresExecution()
    }


    def "sur une erreur appelle tout de même la synchronisation"() {
        given:
        def handler = new FausseCommandeHandler()
        handler.renvoieException()
        def synchronisationBus = Mock(SynchronisationBus)
        def bus = busAvec(handler, synchronisationBus)

        when:
        bus.poste new FausseCommande()

        then:
        1 * synchronisationBus.surErreur()
        then:
        1 * synchronisationBus.finalement()
    }


    def "retourne le résultat d'une commande"() {
        given:
        def handler = new FausseCommandeHandler()
        def bus = busAvec(handler)

        when:
        final ListenableFuture<ResultatCommande<String>> promesse = bus.poste(new FausseCommande())

        then:
        promesse != null
        final ResultatCommande<String> réponse = Futures.getUnchecked(promesse)
        réponse.isSucces()
        réponse.donnees() == "42"
    }

    def "retourne un résultat sur erreur"() {
        setup:
        def handler = new FausseCommandeHandler()
        handler.renvoieException();
        def bus = busAvec(handler);

        when:
        final ListenableFuture<ResultatCommande<String>> promesse = bus.poste(new FausseCommande())

        then:
        promesse != null
        final ResultatCommande<String> réponse = Futures.getUnchecked(promesse)
        réponse != null
        réponse.isErreur()
        réponse.erreur() instanceof RuntimeException
        réponse.erreur().message == "Ceci est une erreur"
    }

    private BusCommandeAsynchrone bus() {
        return new BusCommandeAsynchrone(Sets.newHashSet(mock(SynchronisationBus.class)), Sets.newHashSet(new FausseCommandeHandler()));
    }

    private BusCommandeAsynchrone busAvec(FausseCommandeHandler handler, SynchronisationBus synchronisationBus) {
        final BusCommandeAsynchrone bus = new BusCommandeAsynchrone(Sets.newHashSet(synchronisationBus), Sets.newHashSet(handler));
        bus.setExecutor(executeur());
        return bus;
    }

    private static ListeningExecutorService executeur() {
        return MoreExecutors.sameThreadExecutor()
    }

    private BusCommandeAsynchrone busAvec(SynchronisationBus synchro) {
        return busAvec(unHandler(), synchro)
    }

    private BusCommandeAsynchrone busAvec(FausseCommandeHandler handler) {
        return busAvec(handler, mock(SynchronisationBus.class))
    }

    private FausseCommandeHandler unHandler() {
        return new FausseCommandeHandler()
    }

    private class FausseCommande implements Commande<String> {

        private FausseCommande() {
        }


    }

    private class FausseCommandeHandler implements HandlerCommande<FausseCommande, String> {

        @Override
        public String execute(FausseCommande commande) {
            commandeReçue = commande;
            if (exception) {
                throw new RuntimeException("Ceci est une erreur");
            }
            return "42";
        }

        @Override
        public Class<FausseCommande> typeCommande() {
            return FausseCommande.class;
        }

        public void renvoieException() {
            this.exception = true;
        }

        private FausseCommande commandeReçue;
        private boolean exception;
    }
}
