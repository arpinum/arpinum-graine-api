package fr.arpinum.graine.commande

import fr.arpinum.graine.bus.Commande
import org.hibernate.validator.constraints.NotEmpty
import spock.lang.Specification

import javax.validation.Validation

class ValidateurCommandeTest extends Specification {

    def factory = Validation.buildDefaultValidatorFactory()
    def validateur = new ValidateurCommande(factory.validator)

    def "peut valider une commande"() {
        when:
        validateur.valide new FauseCommande("")

        then:
        thrown(ValidationException)
    }

    def "peut donner la cause de l'erreur"() {
        when:
        validateur.valide new FauseCommande("")

        then:
        ValidationException exception = thrown()
        !exception.messages().empty
        exception.messages()[0]
    }

    def "appelle la validation en début de synchronisation avec le bus"() {
        when:
        validateur.avantExecution new FauseCommande("")

        then:
        thrown(ValidationException)
    }

    private static class FauseCommande implements Commande<String> {
        @NotEmpty
        String nom

        FauseCommande(String nom) {
            this.nom = nom
        }
    }
}
