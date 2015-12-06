package fr.arpinum.graine.web.restlet;

import com.google.common.base.Joiner;
import org.restlet.*;
import org.restlet.data.Header;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.util.UUID;

public class ResourceInitializer {

    public static ResourceInitializer with(ServerResource ressource) {
        return new ResourceInitializer(ressource);
    }

    private ResourceInitializer(ServerResource resource) {
        headers = new Series<>(Header.class);
        request.getAttributes().putIfAbsent("org.restlet.http.headers", headers);
        this.resource = resource;
    }

    public ResourceInitializer withQuery(String key, String value) {
        query = Joiner.on("&").join(query, String.format("%s=%s", key, value));
        return this;
    }

    public ResourceInitializer withHeader(String key, String value) {
        headers.add(key, value);
        return this;
    }

    public ResourceInitializer withConnectedUser() {
        return withConnectedUser(UUID.randomUUID());
    }

    public ResourceInitializer withConnectedUser(UUID userId) {
        return withParam("currentUser", userId);
    }

    public ResourceInitializer withParam(String clef, Object valeur) {
        request.getAttributes().put(clef, valeur);
        return this;
    }

    public void initialize() {
        request.setResourceRef("http:localhost?" + query);
        resource.init(new Context(), request, new Response(request));
    }

    private final ServerResource resource;
    private final Series<Header> headers;
    private final Request request = new Request();
    private String query = "";

}
