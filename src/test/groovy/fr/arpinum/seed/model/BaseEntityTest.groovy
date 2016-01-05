package fr.arpinum.seed.model

import spock.lang.Specification

class BaseEntityTest extends Specification {

    def "two entities with the same id are the same"() {
        given:
        def firstEntity = new anEntity("1")
        def secondEntity = new anEntity("1")

        expect:
        firstEntity == secondEntity
    }

    def "two entities with two differents ids are differents"() {
        given:
        def firstEntity = new anEntity("1")
        def secondEntity = new anEntity("2")

        expect:
        firstEntity != secondEntity
    }


    class anEntity extends BaseEntity<String> {

        anEntity(String id) {
            super(id);
        }

    }
}
