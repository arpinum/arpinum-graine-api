package arpinum.infrastructure.bus

import arpinum.ddd.evenement.Synchrone
import com.google.common.collect.Sets
import spock.lang.Specification

import static org.mockito.Mockito.mock

class AsynchronousBusTest extends Specification {

    def "can execute command"() {
        given:
        def handler = anHandler()
        def bus = busWith(handler)
        def command = new FakeMessage()

        when:
        bus.dispatch(command).blockingFirst()

        then:
        handler.receivedCommand == command
    }

    def "encapsulates with syncs"() {
        given:
        def synchro = Mock(SynchronisationBus)
        def bus = busWith(synchro)
        def command = new FakeMessage()

        when:
        bus.dispatch(command).blockingFirst()

        then:
        1 * synchro.beforeExecution(command)
        then:
        1 * synchro.afterExecution()
        then:
        1 * synchro.atLast()
    }


    def "on error call sync"() {
        given:
        def handler = new FakeCommandHandler()
        handler.throwsException()
        def synchronisationBus = Mock(SynchronisationBus)
        def bus = busWith(handler, synchronisationBus)

        when:
        bus.dispatch(new FakeMessage()).blockingFirst()

        then:
        thrown RuntimeException
        1 * synchronisationBus.onError()
        1 * synchronisationBus.atLast()
    }


    def "returns command result"() {
        given:
        def handler = new FakeCommandHandler()
        def bus = busWith(handler)

        when:
        def result = bus.dispatch(new FakeMessage()).blockingFirst()

        then:
        result == "42"
    }

    def "creates fake handler on empty"() {
        given:
        def bus = emptyBus()

        when:
        def result = bus.dispatch(new FakeMessage())

        then:
        result != null
    }

    def "can execute many handlers"() {
        given:
        def captors = [
                new FakeCommandHandler(),
                new FakeCommandHandler()
        ]
        def bus = new AsynchronousBus(Sets.newHashSet(), captors) {}
        def message = new FakeMessage()

        when:
        def result = bus.dispatch(message).blockingIterable()

        then:
        result.size() == 2
        result[0] == "42"
        result[1] == "42"
    }

    def emptyBus() {
        new AsynchronousBus(Sets.newHashSet(), Sets.newHashSet()) {}
    }


    private AsynchronousBus busWith(handler, SynchronisationBus synchronisationBus) {
        final AsynchronousBus bus = new AsynchronousBus(Sets.newHashSet(synchronisationBus), Sets.newHashSet(handler)) {

        }
        return bus
    }

    private AsynchronousBus busWith(SynchronisationBus synchro) {
        return busWith(anHandler(), synchro)
    }

    private AsynchronousBus busWith(handler) {
        busWith(handler, mock(SynchronisationBus.class))
    }

    private FakeCommandHandler anHandler() {
        new FakeCommandHandler()
    }

    private class FakeMessage implements Message<String> {

        private FakeMessage() {
        }


    }

    private class FakeCommandHandler implements MessageCaptor<FakeMessage, String> {

        @Override
        String execute(FakeMessage command) {
            receivedCommand = command
            if (exception) {
                throw new RuntimeException("Ceci est une erreur")
            }
            return '42'
        }

        void throwsException() {
            this.exception = true
        }

        private FakeMessage receivedCommand
        private boolean exception
    }

    @Synchrone
    private class SyncCommandHandler implements MessageCaptor<FakeMessage, String> {

        @Override
        String execute(FakeMessage command) {
            return ''
        }
    }
}
