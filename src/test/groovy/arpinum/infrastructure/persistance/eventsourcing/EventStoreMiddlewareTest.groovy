package arpinum.infrastructure.persistance.eventsourcing

import arpinum.command.Command
import arpinum.ddd.event.Event
import arpinum.ddd.event.EventStore
import io.vavr.Tuple
import io.vavr.collection.List
import spock.lang.Specification

class EventStoreMiddlewareTest extends Specification {

    def eventStore = Mock(EventStore)

    def middleware = new EventStoreMiddleware(eventStore)

    def "it calls next and saves results"() {
        given:
        def command = {} as Command<String>
        def event = new Event<String>(){

        }
        def result = Tuple.of("hello", List.of(event))

        when:
        def value = middleware.intercept(command, { result })

        then:
        1 * eventStore.save(List.of(event))
        value == result
    }

}
