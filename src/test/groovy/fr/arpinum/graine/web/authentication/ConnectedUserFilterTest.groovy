package fr.arpinum.graine.web.authentication

import com.auth0.jwt.JWTSigner
import com.google.common.collect.Maps
import fr.arpinum.graine.infrastructure.date.Dates
import org.restlet.Request
import org.restlet.Response
import org.restlet.data.Header
import org.restlet.routing.Filter
import org.restlet.util.Series
import spock.lang.Specification

import java.time.temporal.ChronoUnit


class ConnectedUserFilterTest extends Specification {

    public static final String KEY = "39EF3CED29692612E55B43B625548776A55693F28FD114DF947C48351EE7596B"
    public static final String ISSUER = "http://jamshake.com"
    public static final String AUDIENCE = "jamshake"
    ConnectedUserFilter filter
    def request, response, headers

    void setup() {
        filter = new ConnectedUserFilter(KEY, ISSUER, AUDIENCE)
        request = new Request()
        response = new Response()
        headers = new Series<Header>(Header.class)
        request.getAttributes().putIfAbsent("org.restlet.http.headers", headers);
    }

    def "sets the user when valid token"() {
        given:
        def user = UUID.randomUUID()
        headers << new Header(ConnectedUserFilter.SESSION_TOKEN, "Bearer " + jwt(user));

        when:
        def result = filter.beforeHandle(request, response)

        then:
        result == Filter.CONTINUE
        request.attributes["currentUser"] == user
    }

    def "rejects request when no token"() {
        when:
        def result = filter.beforeHandle(request, response)

        then:
        result == Filter.CONTINUE
        request.attributes["currentUser"] == null
    }

    def "does not reject when expired"() {
        given:
        headers << new Header(ConnectedUserFilter.SESSION_TOKEN, "Bearer " +  jwt());


        when:
        def result = filter.beforeHandle(request, response)

        then:
        result == Filter.CONTINUE
        request.attributes["currentUser"] != null
    }

    private String jwt(UUID user = UUID.randomUUID()) {
        final JWTSigner signer = new JWTSigner(KEY);
        Map<String, Object> claims = Maps.newConcurrentMap();
        claims.put("sub", user.toString());
        claims.put("iss", ISSUER);
        claims.put("aud", AUDIENCE);
        claims.put("exp", Dates.instantNow().plus(1, ChronoUnit.HOURS).getEpochSecond());
        claims.put("isa", Dates.instantNow().getEpochSecond());
        def token = signer.sign(claims)
        token
    }
}
