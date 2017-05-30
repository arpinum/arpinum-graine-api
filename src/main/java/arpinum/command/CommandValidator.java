package arpinum.command;


import arpinum.ddd.event.Event;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CommandValidator implements CommandMiddleware {

    @Inject
    public CommandValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public Tuple2<?, Seq<Event>> intercept(Command<?> command, Supplier<Tuple2<?, Seq<Event>>> next) {
        validate(command);
        return next.get();
    }

    public void validate(Command<?> command) {
        Set<ConstraintViolation<Command<?>>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ValidationException(enMessages(violations));
        }
    }

    private List<String> enMessages(Set<ConstraintViolation<Command<?>>> violations) {
        return violations.stream().map((violation) -> violation.getPropertyPath() + " " + violation.getMessage()).collect(Collectors.toList());
    }

    private final Validator validator;
}
