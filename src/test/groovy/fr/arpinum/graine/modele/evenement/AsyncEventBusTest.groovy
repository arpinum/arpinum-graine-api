package fr.arpinum.graine.modele.evenement

import com.google.common.collect.Sets
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import spock.lang.Specification

class AsyncEventBusTest extends Specification {

    def "n'exécute pas immédiattement l'évènement"() {
        given:
        def capteur = new FauxCapteurEvement()
        AsyncEventBus bus = busAvec(capteur)

        when:
        bus.publish(new FauxEvenement())

        then:
        !capteur.appelé
    }

    def "exécute les évènements après les commandes"() {
        given:
        def capteur = new FauxCapteurEvement()
        AsyncEventBus bus = busAvec(capteur)

        when:
        bus.publish(new FauxEvenement())
        bus.afterExecution()

        then:
        capteur.appelé
    }

    def "peut exécuter en synchrone un capteur"() {
        given:
        AsyncEventBus bus = busAvec(new FauxCapteurEvementSynchrone())
        def future = Mock(ListenableFuture)
        bus.executor = unExécuteurQuiRetourne(future)

        when:
        bus.publish(new FauxEvenementSynchrone())
        bus.afterExecution()

        then:
        0 * future.get()
    }

    private unExécuteurQuiRetourne(future) {
        [submit: {
            return future
        }] as ListeningExecutorService
    }

    private busAvec(EventCaptor<? extends Event> capteur) {
        def bus = new AsyncEventBus(Sets.newHashSet(), Sets.newHashSet(capteur))
        bus.setExecutor(MoreExecutors.sameThreadExecutor())
        return bus
    }

    public static class FauxEvenement implements Event {

    }

    public static class FauxCapteurEvement implements EventCaptor<FauxEvenement> {

        boolean appelé

        @Override
        void executeEvent(FauxEvenement evenement) {
            appelé = true
        }
    }

    @Synchrone
    public static class FauxEvenementSynchrone implements Event {

    }

    public static class FauxCapteurEvementSynchrone implements EventCaptor<FauxEvenementSynchrone> {

        boolean appelé


        @Override
        void executeEvent(FauxEvenementSynchrone evenement) {
            appelé = true
        }
    }
}
