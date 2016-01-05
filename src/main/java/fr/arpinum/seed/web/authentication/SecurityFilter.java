package fr.arpinum.seed.web.authentication;

import org.restlet.*;
import org.restlet.data.Status;
import org.restlet.routing.Filter;

public class SecurityFilter extends Filter {

    @Override
    protected int beforeHandle(Request request, Response response) {
        if (request.getAttributes().containsKey("currentUser")) {
            return Filter.CONTINUE;
        }
        response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
        return Filter.STOP;
    }

}
