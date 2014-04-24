package fr.arpinum.graine.modele.evenement
import com.google.common.collect.Sets
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import spock.lang.Specification

class BusEvenementAsynchroneTest extends Specification {

    def "n'exécute pas immédiattement l'évènement"() {
        given:
        def capteur = new FauxCapteurEvement()
        BusEvenementAsynchrone bus = busAvec(capteur)

        when:
        bus.publie(new FauxEvenement())

        then:
        !capteur.appelé
    }

    def "exécute les évènements après les commandes"() {
        given:
        def capteur = new FauxCapteurEvement()
        BusEvenementAsynchrone bus = busAvec(capteur)

        when:
        bus.publie(new FauxEvenement())
        bus.apresExecution()

        then:
        capteur.appelé
    }

    def "peut exécuter en synchrone un capteur"() {
        given:

        BusEvenementAsynchrone bus = busAvec(new FauxCapteurEvementSynchrone())
        def future = Mock(ListenableFuture)
        bus.executor = unExécuteurQuiRetourne(future)

        when:
        bus.publie(new FauxEvenementSynchrone())
        bus.apresExecution()

        then:
        1 * future.get()
    }

    private unExécuteurQuiRetourne(future) {
        def service = [submit: {
            return future
        }] as ListeningExecutorService
        service
    }

    private busAvec(CapteurEvenement<?> capteur) {
        def bus = new BusEvenementAsynchrone(Sets.newHashSet(), Sets.newHashSet(capteur))
        bus.setExecutor(MoreExecutors.sameThreadExecutor())
        return bus
    }

    public static class FauxEvenement implements Evenement {

    }

    public static class FauxCapteurEvement implements CapteurEvenement<FauxEvenement> {

        boolean appelé

        @Override
        void executeEvenement(FauxEvenement evenement) {
            appelé = true
        }
    }

    @Synchrone
    public static class FauxEvenementSynchrone implements Evenement {

    }

    public static class FauxCapteurEvementSynchrone implements CapteurEvenement<FauxEvenementSynchrone> {

        boolean appelé


        @Override
        void executeEvenement(FauxEvenementSynchrone evenement) {
            appelé = true
        }
    }
}
