package arpinum.infrastructure.bus;


import io.reactivex.Observable;

public interface Bus {

    <TReponse> Observable<TReponse> dispatch(Message<TReponse> message);

}
