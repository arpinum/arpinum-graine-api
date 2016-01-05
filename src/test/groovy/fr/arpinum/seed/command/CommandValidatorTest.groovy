package fr.arpinum.seed.command

import fr.arpinum.seed.infrastructure.bus.Message
import org.hibernate.validator.constraints.NotEmpty
import spock.lang.Specification

import javax.validation.Validation

class CommandValidatorTest extends Specification {

    def factory = Validation.buildDefaultValidatorFactory()
    def validator = new CommandValidator(factory.validator)

    def "can validate a command"() {
        when:
        validator.validate new FakeMessage("")

        then:
        thrown(ExceptionValidation)
    }

    def "can give the cause of error"() {
        when:
        validator.validate new FakeMessage("")

        then:
        ExceptionValidation exception = thrown()
        !exception.messages().empty
        exception.messages()[0]
    }

    def "call the validation at start of bus synchronization"() {
        when:
        validator.beforeExecution new FakeMessage("")

        then:
        thrown(ExceptionValidation)
    }

    private static class FakeMessage implements Message<String> {
        @NotEmpty
        String name

        FakeMessage(String name) {
            this.name = name
        }
    }
}
