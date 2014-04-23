package fr.arpinum.graine.modele.evenement

import com.google.common.collect.Sets
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

    private busAvec(FauxCapteurEvement capteur) {
        def bus = new BusEvenementAsynchrone(Sets.newHashSet(), Sets.newHashSet(capteur))
        bus.executor = MoreExecutors.sameThreadExecutor()
        bus
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
}
