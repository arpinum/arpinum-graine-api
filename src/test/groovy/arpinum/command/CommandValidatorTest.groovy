package arpinum.command

import org.hibernate.validator.constraints.NotEmpty
import arpinum.infrastructure.bus.Message
import spock.lang.Specification

import javax.validation.Validation

class CommandValidatorTest extends Specification {

    def factory = Validation.buildDefaultValidatorFactory()
    def validateur = new CommandValidator(factory.validator)

    def "peut valider une commande"() {
        when:
        validateur.valide new FauxMessage("")

        then:
        thrown(ValidationException)
    }

    def "peut donner la cause de l'erreur"() {
        when:
        validateur.valide new FauxMessage("")

        then:
        ValidationException exception = thrown()
        !exception.messages().empty
        exception.messages()[0]
    }

    def "appelle la validation en début de synchronisation avec le bus"() {
        when:
        validateur.beforeExecution new FauxMessage("")

        then:
        thrown(ValidationException)
    }

    private static class FauxMessage implements Message<String> {
        @NotEmpty
        String nom

        FauxMessage(String nom) {
            this.nom = nom
        }
    }
}
