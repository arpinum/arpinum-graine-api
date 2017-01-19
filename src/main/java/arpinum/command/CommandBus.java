package arpinum.command;

import arpinum.infrastructure.bus.Message;
import io.reactivex.Single;

public interface CommandBus {

    <TReponse> Single<TReponse> send(Message<TReponse> message);

}
