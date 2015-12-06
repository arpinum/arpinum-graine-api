package fr.arpinum.graine.web.restlet.status;


import org.restlet.data.Status;
import org.restlet.representation.Representation;

public interface ExceptionResolver {

    boolean canResolve(Throwable throwable);

    Status status(Throwable cause);

    Representation representation(Throwable throwable);
}
