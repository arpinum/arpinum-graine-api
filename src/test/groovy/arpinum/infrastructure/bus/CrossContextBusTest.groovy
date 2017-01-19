package arpinum.infrastructure.bus

import arpinum.ddd.evenement.Event
import spock.lang.Specification

import java.util.function.Consumer

class CrossContextBusTest extends Specification {

    def bus = new CrossContextBus()

    def "publish and subscribe"() {
        given:
        def subscriber = Mock(Consumer) as Consumer<FakeEvent>
        bus.subscribe(subscriber)
        def event = new FakeEvent()

        when:
        bus.publish(new Event() {})
        bus.publish(event)

        then:
        1 * subscriber.accept(event)
    }

    static class FakeEvent extends Event<String> {

    }
}
