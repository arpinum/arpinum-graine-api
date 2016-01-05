package fr.arpinum.seed.web.restlet.status;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.representation.*;
import org.restlet.service.*;

import java.util.*;

public class ApplicationStatusService extends StatusService {
    @Override
    public Status getStatus(Throwable throwable, Request request, Response response) {
        Optional<ExceptionResolver> resolver = resolver(throwable.getCause());
        if (resolver.isPresent()) {
            return resolver.get().status(throwable.getCause());
        }
        return super.getStatus(throwable, request, response);
    }

    @Override
    public Representation getRepresentation(Status status, Request request, Response response) {
        Optional<ExceptionResolver> resolveur = resolver(status.getThrowable());
        if (resolveur.isPresent()) {
            return resolveur.get().representation(status.getThrowable());
        }
        return super.getRepresentation(status, request, response);
    }

    private Optional<ExceptionResolver> resolver(Throwable throwable) {
        for (ExceptionResolver resolveur : resolvers) {
            if (resolveur.canResolve(throwable)) return Optional.of(resolveur);
        }
        return Optional.absent();
    }

    private List<ExceptionResolver> resolvers = Lists.newArrayList(
            new ValidationExceptionResolver(),
            new NotAllowedExceptionResolver(),
            new BusinessErrorResolver()
    );

}
