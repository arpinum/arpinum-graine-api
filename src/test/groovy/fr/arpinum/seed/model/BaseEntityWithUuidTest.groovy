package fr.arpinum.seed.model

import spock.lang.Specification

class BaseEntityWithUuidTest extends Specification {

    def "génère un identifiant par défaut"() {
        given:
        def uneEntité = new UneEntite()

        expect:
        uneEntité.getId() != null
    }

    class UneEntite extends BaseEntityWithUuid {

    }
}
