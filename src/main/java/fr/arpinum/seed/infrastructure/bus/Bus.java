package fr.arpinum.seed.infrastructure.bus;

import com.google.common.util.concurrent.ListenableFuture;

public interface Bus {

    <TResponse> ListenableFuture<ExecutionResult<TResponse>> send(Message<TResponse> message);

    <TResponse> ExecutionResult<TResponse> sendAndWaitForResponse(Message<TResponse> message);

}
