package fr.arpinum.graine.infrastructure.reactor;

import com.google.common.collect.*;
import org.junit.rules.*;
import reactor.*;
import reactor.bus.*;

import java.util.*;

import static reactor.bus.selector.Selectors.*;

public class WithGlobalEventBus extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        Environment.initializeIfEmpty();
        instance = EventBus.create(Environment.dispatcher("sync"));
    }

    public void listenTo(String key) {
        instance.on($(key), e -> events.put(key, (Event<String>) e));
    }

    public Optional<Event<String>> lastOf(String key) {
        return events.get(key).stream().findFirst();
    }

    public EventBus instance;
    private ListMultimap<String, Event<String>> events = ArrayListMultimap.create();
}
