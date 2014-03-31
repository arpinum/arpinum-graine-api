package fr.arpinum.seed.commande;

import fr.arpinum.seed.bus.Commande;
import fr.arpinum.seed.bus.SynchronisationBus;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidateurCommande implements SynchronisationBus {

    @Inject
    public ValidateurCommande(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void avantExecution(Commande<?> commande) {
        valide(commande);
    }

    public void valide(Commande<?> commande) {
        Set<ConstraintViolation<Commande<?>>> violations = validator.validate(commande);
        if (!violations.isEmpty()) {
            throw new ValidationException(enMessages(violations));
        }
    }

    private List<String> enMessages(Set<ConstraintViolation<Commande<?>>> violations) {
        return violations.stream().map((violation) -> violation.getPropertyPath() + " " + violation.getMessage()).collect(Collectors.toList());
    }

    @Override
    public void apresExecution() {

    }

    @Override
    public void finalement() {

    }

    private final Validator validator;
}
