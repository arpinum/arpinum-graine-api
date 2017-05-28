package arpinum.infrastructure.bus.event

import arpinum.command.Command
import arpinum.ddd.event.Event
import arpinum.ddd.event.EventBus
import io.vavr.Tuple
import io.vavr.collection.List
import spock.lang.Specification


class EventDispatcherMiddlewareTest extends Specification {

    def eventBus = Mock(EventBus)

    def middleware = new EventDispatcherMiddleware(eventBus)

    def "it dispatches and returns results"() {
        given:
        def command = {} as Command<String>
        def event = new Event<String>() {

        }
        def result = Tuple.of("h", List.of(event))

        when:
        def value = middleware.intercept(command, {result})

        then:
        1 * eventBus.publish(result._2)
        value == result
    }
}
