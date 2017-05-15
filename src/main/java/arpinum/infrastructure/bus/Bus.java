package arpinum.infrastructure.bus;


import io.vavr.concurrent.Future;

public interface Bus {

    <TReponse> Future<TReponse> dispatch(Message<TReponse> message);

}
