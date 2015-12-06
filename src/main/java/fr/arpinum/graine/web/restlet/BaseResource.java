package fr.arpinum.graine.web.restlet;

import com.google.common.base.Throwables;
import fr.arpinum.graine.infrastructure.bus.ExecutionResult;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

import java.util.UUID;

public class BaseResource extends ServerResource {

    protected <T> T getDataOrFail(ExecutionResult<T> executionResult) {
        return getDataOrFail(executionResult, Status.SUCCESS_OK);
    }
    protected <T> T getDataOrFail(ExecutionResult<T> executionResult, Status statusToReturn) {
        if(executionResult == null) {
            return null;
        }
        if(executionResult.isSuccess()) {
            setStatus(statusToReturn);
            return executionResult.data();
        }
        Throwables.propagate(executionResult.error());
        return null;
    }


    protected UUID getConnectedUserId() {
        return (UUID) getRequestAttributes().get("currentUser");
    }
}
