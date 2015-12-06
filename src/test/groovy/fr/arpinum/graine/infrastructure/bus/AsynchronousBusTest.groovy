package fr.arpinum.graine.infrastructure.bus

import com.google.common.collect.Sets
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import spock.lang.Specification

import java.util.concurrent.ExecutorService

import static org.mockito.Mockito.*

public class AsynchronousBusTest extends Specification {


    def "peut exécuter une commande"() {
        given:
        def handler = unHandler()
        def bus = busAvec(handler)
        def commande = new FauxMessage()

        when:
        bus.send(commande)

        then:
        handler.commandeReçue == commande
    }

    def "est asynchrone"() {
        given:
        def executor = Mock(ExecutorService.class)
        def bus = bus()
        bus.setExecutor(executor)

        when:
        bus.send(new FauxMessage())

        then:
        1 * executor.execute(!null)
    }


    def "encapsule les commandes dans les synchronisations"() {
        given:
        def synchro = Mock(SynchronisationBus)
        def bus = busAvec(synchro)
        def commande = new FauxMessage()

        when:
        bus.send(commande)

        then:
        1 * synchro.beforeExecution(commande)
        then:
        1 * synchro.afterExecution()
    }


    def "sur une erreur appelle tout de même la synchronisation"() {
        given:
        def handler = new FausseCommandeCapteur()
        handler.renvoieException()
        def synchronisationBus = Mock(SynchronisationBus)
        def bus = busAvec(handler, synchronisationBus)

        when:
        bus.send new FauxMessage()

        then:
        1 * synchronisationBus.onError()
        then:
        1 * synchronisationBus.atLast()
    }


    def "retourne le résultat d'une commande"() {
        given:
        def handler = new FausseCommandeCapteur()
        def bus = busAvec(handler)

        when:
        final ListenableFuture<ExecutionResult<String>> promesse = bus.send(new FauxMessage())

        then:
        promesse != null
        final ExecutionResult<String> réponse = Futures.getUnchecked(promesse)
        réponse.isSuccess()
        réponse.data() == "42"
    }

    def "peut retourner directement le résultat"() {
        given:
        def handler = new FausseCommandeCapteur()
        def bus = busAvec(handler)

        when:
        def résultat = bus.sendAndWaitForResponse(new FauxMessage())

        then:
        résultat != null
    }

    def "retourne un résultat sur erreur"() {
        setup:
        def handler = new FausseCommandeCapteur()
        handler.renvoieException();
        def bus = busAvec(handler);

        when:
        final ListenableFuture<ExecutionResult<String>> promesse = bus.send(new FauxMessage())

        then:
        promesse != null
        final ExecutionResult<String> réponse = Futures.getUnchecked(promesse)
        réponse != null
        réponse.isError()
        réponse.error() instanceof RuntimeException
        réponse.error().message == "Ceci est une erreur"
    }

    def "retourne une erreur si aucun handler"() {
        given:
        def bus = unBusVide()

        when:
        def promesse = bus.send(new FauxMessage())

        then:
        promesse != null
        def resultatExecution = promesse.get()
        resultatExecution.isError()
        resultatExecution.error() instanceof ErreurBus
    }

    def "peut exécuter plusieurs handlers"() {
        given:
        def capteurs = [new FausseCommandeCapteur(), new FausseCommandeCapteur()]
        def bus = new AsynchronousBus(Sets.newHashSet(), capteurs) {}
        bus.setExecutor(executeur())
        def message = new FauxMessage()

        when:
        bus.send(message)

        then:
        capteurs[0].commandeReçue == message
        capteurs[1].commandeReçue == message
    }

    def unBusVide() {
        new AsynchronousBus(Sets.newHashSet(), Sets.newHashSet()) {}
    }

    private AsynchronousBus bus() {
        new AsynchronousBus(Sets.newHashSet(mock(SynchronisationBus.class)), Sets.newHashSet(new FausseCommandeCapteur())) {
        };
    }

    private AsynchronousBus busAvec(FausseCommandeCapteur handler, SynchronisationBus synchronisationBus) {
        final AsynchronousBus bus = new AsynchronousBus(Sets.newHashSet(synchronisationBus), Sets.newHashSet(handler)) {

        }
        bus.setExecutor(executeur())
        return bus;
    }

    private static ListeningExecutorService executeur() {
        return MoreExecutors.sameThreadExecutor()
    }

    private AsynchronousBus busAvec(SynchronisationBus synchro) {
        return busAvec(unHandler(), synchro)
    }

    private AsynchronousBus busAvec(FausseCommandeCapteur handler) {
        busAvec(handler, mock(SynchronisationBus.class))
    }

    private FausseCommandeCapteur unHandler() {
        new FausseCommandeCapteur()
    }

    private class FauxMessage implements Message<String> {

        private FauxMessage() {
        }


    }

    private class FausseCommandeCapteur implements MessageCaptor<FauxMessage, String> {

        @Override
        public String execute(FauxMessage commande) {
            commandeReçue = commande;
            if (exception) {
                throw new RuntimeException("Ceci est une erreur");
            }
            return "42";
        }

        public void renvoieException() {
            this.exception = true;
        }

        private FauxMessage commandeReçue;
        private boolean exception;
    }
}
