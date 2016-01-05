package fr.arpinum.seed.web.restlet.status

import com.google.common.collect.Lists
import fr.arpinum.seed.command.ExceptionValidation
import org.restlet.ext.json.JsonRepresentation
import spock.lang.Specification

class ExceptionValidationResolverTest extends Specification {

    def resolver = new ValidationExceptionResolver()

    def "résoud bien les exceptions de validation"() {
        expect:
        resolver.canResolve(new ExceptionValidation(Lists.newArrayList()))
    }

    def "représente correctement en json"() {
        given:
        def exception = new ExceptionValidation(Lists.newArrayList("message", "autre"))

        when:
        def representation = resolver.representation(exception)

        then:
        representation instanceof JsonRepresentation
        JsonRepresentation json = representation
        def errors = json.getJsonObject().getJSONArray("errors")
        errors != null
        errors.length() == 2
        errors.getJSONObject(0).getString("message") == "message"
        errors.getJSONObject(1).getString("message") == "autre"
    }
}
