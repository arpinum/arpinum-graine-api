package arpinum.infrastructure.bus;


import io.vavr.control.Try;

public interface BusMiddleware {

    <T> Try<T> handle(Message<T> message, BusMiddleware next);

}
