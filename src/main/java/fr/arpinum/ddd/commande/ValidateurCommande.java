package fr.arpinum.ddd.commande;

import fr.arpinum.bus.Commande;
import fr.arpinum.bus.SynchronisationBus;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class ValidateurCommande implements SynchronisationBus {

    public ValidateurCommande(Validator validator) {
        this.validator = validator;
    }

    public void valide(Commande<?> commande) {
        Set<ConstraintViolation<Commande<?>>> violations = validator.validate(commande);
        if (!violations.isEmpty()) {
            throw new ValidationException();
        }
    }

    @Override
    public void avantExecution(Commande<?> commande) {

    }

    @Override
    public void apresExecution() {

    }

    @Override
    public void finalement() {

    }

    private final Validator validator;
}
