package fr.arpinum.seed.infrastructure.persistence.mongo

import org.junit.Rule
import spock.lang.Specification

class MongoRepositoryTest extends Specification {

    @Rule
    public WithMongolink mongolink = WithMongolink.withPackage("fr.arpinum.seed.infrastructure.persistence.mongo.mapping")

    def setup() {
        repository = new FakeRootRepository(mongolink.currentSession())
    }

    def "can add"() {
        when:
        repository.add(new FakeAggregate("1"))
        mongolink.cleanSession();

        then:
        def elementFound = mongolink.collection("fakeaggregate").findOne(_id: "1")
        elementFound != null
    }


    def "can delete"() {
        given:
        def root = new FakeAggregate("1")
        repository.add(root)

        when:
        repository.delete(root)
        mongolink.cleanSession()

        then:
        repository.get("1") == null
    }


    FakeRootRepository repository

}
