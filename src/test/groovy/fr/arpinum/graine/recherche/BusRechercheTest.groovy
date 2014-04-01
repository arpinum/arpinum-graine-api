package fr.arpinum.graine.recherche

import com.github.fakemongo.Fongo
import org.jongo.Jongo
import spock.lang.Specification

class BusRechercheTest extends Specification {

    def fongo = new Fongo("Test Serveur")
    def jongo = new Jongo(fongo.getDB("test"))


    def "peut exécuter une recherche"() {
        expect:
        true == true
    }

}
