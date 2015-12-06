package fr.arpinum.graine.commande;

import fr.arpinum.graine.infrastructure.bus.Message;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandValidator implements SynchronisationCommande {

    @Inject
    public CommandValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void beforeExecution(Message<?> commande) {
        valide(commande);
    }

    public void valide(Message<?> commande) {
        Set<ConstraintViolation<Message<?>>> violations = validator.validate(commande);
        if (!violations.isEmpty()) {
            throw new ValidationException(enMessages(violations));
        }
    }

    private List<String> enMessages(Set<ConstraintViolation<Message<?>>> violations) {
        return violations.stream().map((violation) -> violation.getPropertyPath() + " " + violation.getMessage()).collect(Collectors.toList());
    }

    private final Validator validator;
}
