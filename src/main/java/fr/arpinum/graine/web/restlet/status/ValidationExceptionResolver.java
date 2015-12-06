package fr.arpinum.graine.web.restlet.status;

import fr.arpinum.graine.commande.ValidationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

public class ValidationExceptionResolver implements ExceptionResolver {


    @Override
    public boolean canResolve(Throwable throwable) {
        return throwable!= null && ValidationException.class.isAssignableFrom(throwable.getClass());
    }

    @Override
    public Status status(Throwable error) {
        return new Status(Status.CLIENT_ERROR_BAD_REQUEST.getCode(), error);
    }

    @Override
    public Representation representation(Throwable throwable) {
        try {
            return new JsonRepresentation(toJson((ValidationException) throwable));
        } catch (JSONException e) {
            return new JsonRepresentation("{}");
        }
    }

    private JSONObject toJson(ValidationException validationException) throws JSONException {
        JSONObject result = new JSONObject();
        JSONArray errors = new JSONArray();
        result.put("errors", errors);
        validationException.messages().forEach((message) -> {
            JSONObject error = new JSONObject();
            putMessage(message, error);
            errors.put(error);
        });
        return result;
    }

    private void putMessage(String message, JSONObject error) {
        try {
            error.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
