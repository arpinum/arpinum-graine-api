package fr.arpinum.seed.web.authentication

import org.restlet.Request
import org.restlet.Response
import org.restlet.data.Header
import org.restlet.data.Status
import org.restlet.routing.Filter
import org.restlet.util.Series
import spock.lang.Specification


class SecurityFilterTest extends Specification {
    SecurityFilter filter
    def request, response, headers

    void setup() {
        filter = new SecurityFilter()
        request = new Request()
        response = new Response()
        headers = new Series<Header>(Header.class)
        request.getAttributes().putIfAbsent("org.restlet.http.headers", headers);
    }

    def "continue if user present"() {
        given:
        request.getAttributes().putIfAbsent("currentUser", UUID.randomUUID());

        when:
        def result = filter.beforeHandle(request, response)

        then:
        result == Filter.CONTINUE
    }

    def "rejects request when no user"() {
        when:
        def result = filter.beforeHandle(request, response)

        then:
        result == Filter.STOP
        response.status == Status.CLIENT_ERROR_UNAUTHORIZED
    }
}
