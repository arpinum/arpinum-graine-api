package arpinum.ddd.event;

import com.google.common.collect.*;

import java.util.*;

public class FakeEventBus implements EventBus {

    public <T extends Event> T lastEvent(final Class<T> type) {
        return (T) evenements.get(type);
    }


    @Override
    public void publish(Event... events) {
        if(events.length == 0) {
            return;
        }
        evenements.put(events[0].getClass(), events[0]);
    }

    private Map<Class<? extends Event>, Event> evenements = Maps.newHashMap();
}
