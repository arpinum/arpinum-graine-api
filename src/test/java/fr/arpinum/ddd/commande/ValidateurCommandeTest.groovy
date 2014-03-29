package fr.arpinum.ddd.commande

import fr.arpinum.bus.Commande
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

    private static class FauseCommande implements Commande<String> {
        @NotEmpty
        String nom

        FauseCommande(String nom) {
            this.nom = nom
        }
    }
}
