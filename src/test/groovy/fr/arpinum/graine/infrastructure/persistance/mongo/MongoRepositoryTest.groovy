package fr.arpinum.graine.infrastructure.persistance.mongo

import org.junit.Rule
import spock.lang.Specification

class MongoRepositoryTest extends Specification {

    @Rule
    public WithMongolink mongolink = WithMongolink.withPackage("fr.arpinum.graine.infrastructure.persistance.mongo.mapping")

    def setup() {
        entrepot = new FakeRootRepository(mongolink.currentSession())
    }

    def "peut ajouter"() {
        when:
        entrepot.add(new FakeAggregate("1"))
        mongolink.cleanSession();

        then:
        def elementTrouvé = mongolink.collection("fakeaggregate").findOne(_id: "1")
        elementTrouvé != null
    }


    def "peut supprimer"() {
        given:
        def racine = new FakeAggregate("1")
        entrepot.add(racine)

        when:
        entrepot.delete(racine)
        mongolink.cleanSession()

        then:
        entrepot.get("1") == null
    }


    FakeRootRepository entrepot

}
