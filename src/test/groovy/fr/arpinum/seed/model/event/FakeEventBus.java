package fr.arpinum.seed.model.event;

import com.google.common.collect.*;

import java.util.*;

public class FakeEventBus implements EventBus {

    @Override
    public void publish(Event... events) {
        Arrays.asList(events).forEach(e -> this.events.put(e.getClass(), e));
    }

    public <T extends Event> T lastEvent(final Class<T> type) {
        return (T) events.get(type);
    }


    private Map<Class<? extends Event>, Event> events = Maps.newHashMap();
}
