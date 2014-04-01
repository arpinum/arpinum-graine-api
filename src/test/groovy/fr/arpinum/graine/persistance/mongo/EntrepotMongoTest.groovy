package fr.arpinum.graine.persistance.mongo

import org.mongolink.test.MongolinkRule
import spock.lang.Specification

class EntrepotMongoTest extends Specification {

    def setup() {
        mongolink = MongolinkRule.withPackage("fr.arpinum.graine.persistance.mongo.mapping")
        mongolink.before()
        entrepot = new EntrepotFausseRacine(mongolink.currentSession)
    }

    def cleanup() {
        mongolink.after()
    }

    def "peut ajouter"() {
        when:
        entrepot.ajoute(new FausseRacine("1"))
        mongolink.cleanSession();

        then:
        def racineTrouvé = entrepot.get("1")
        racineTrouvé != null
        racineTrouvé.id == "1"
    }

    def "peut supprimer"() {
        given:
        def racine = new FausseRacine("1")
        entrepot.ajoute(racine)

        when:
        entrepot.supprime(racine)
        mongolink.cleanSession()

        then:
        entrepot.get("1") == null
    }

    MongolinkRule mongolink
    EntrepotFausseRacine entrepot

}
