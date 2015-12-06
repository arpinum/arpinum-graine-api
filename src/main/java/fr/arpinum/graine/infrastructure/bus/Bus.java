package fr.arpinum.graine.infrastructure.bus;

import com.google.common.util.concurrent.ListenableFuture;

public interface Bus {

    <TReponse> ListenableFuture<ExecutionResult<TReponse>> send(Message<TReponse> message);

    <TReponse> ExecutionResult<TReponse> sendAndWaitForResponse(Message<TReponse> message);

}
