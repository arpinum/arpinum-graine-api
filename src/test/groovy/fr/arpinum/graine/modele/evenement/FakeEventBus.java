package fr.arpinum.graine.modele.evenement;

import com.google.common.collect.*;

import java.util.*;

public class FakeEventBus implements EventBus {

    @Override
    public void publish(Event... events) {
        Arrays.asList(events).forEach(e -> evenements.put(e.getClass(), e));
    }

    public <T extends Event> T lastEvent(final Class<T> type) {
        return (T) evenements.get(type);
    }


    private Map<Class<? extends Event>, Event> evenements = Maps.newHashMap();
}
