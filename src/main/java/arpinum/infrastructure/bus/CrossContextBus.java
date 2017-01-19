package arpinum.infrastructure.bus;


import arpinum.ddd.evenement.*;
import io.reactivex.subjects.PublishSubject;

import java.util.function.Consumer;

public class CrossContextBus implements EventBus {

    @Override
    public void publish(Event... events) {
        for (Event event : events) {
            rxBus.onNext(event);
        }
    }

    public <T extends Event> void subscribe(Consumer<T> subscriber) {
        rxBus.subscribe(event -> subscriber.accept((T) event));
    }

    private final PublishSubject<Event> rxBus = PublishSubject.create();
}
