package fr.arpinum.graine.web.authentication;


import com.auth0.jwt.JWTVerifier;
import org.restlet.*;
import org.restlet.data.Header;
import org.restlet.routing.Filter;
import org.restlet.util.Series;

import javax.inject.Inject;
import java.util.*;

public class ConnectedUserFilter extends Filter {

    @Inject
    public ConnectedUserFilter(String key, String issuer, String audience) {
        this.key = key;
        this.issuer = issuer;
        this.audience = audience;
    }


    @Override
    protected int beforeHandle(Request request, Response response) {
        final String token = ((Series<Header>) request.getAttributes().get("org.restlet.http.headers")).getFirstValue(SESSION_TOKEN, true);
        final Optional<String> eventualToken = Optional.ofNullable(token);
        if (!eventualToken.isPresent()) {
            return Filter.CONTINUE;
        }
        final Optional<UUID> eventualUserId = checkToken(eventualToken.get().split(" ")[1]);
        if (!eventualUserId.isPresent()) {
            return Filter.CONTINUE;
        }
        request.getAttributes().putIfAbsent("currentUser", eventualUserId.get());
        return Filter.CONTINUE;
    }

    public Optional<UUID> checkToken(String token) {
        try {
            final JWTVerifier jwtVerifier = new JWTVerifier(key, audience, issuer);
            final Map<String, Object> claims = jwtVerifier.verify(token);
            return Optional.of(UUID.fromString(claims.get("sub").toString()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static final String SESSION_TOKEN = "Authorization";

    private final String key;
    private final String issuer;
    private final String audience;
}
