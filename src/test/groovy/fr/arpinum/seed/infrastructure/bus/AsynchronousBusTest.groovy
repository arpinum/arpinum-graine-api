package fr.arpinum.seed.infrastructure.bus

import com.google.common.collect.Sets
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import spock.lang.Specification

import java.util.concurrent.ExecutorService

import static org.mockito.Mockito.*

public class AsynchronousBusTest extends Specification {

    def "can execute a command"() {
        given:
        def handler = aHandler()
        def bus = busWith(handler)
        def command = new FakeMessage()

        when:
        bus.send(command)

        then:
        handler.commandReceived == command
    }

    def "is asynchronous"() {
        given:
        def executor = Mock(ExecutorService.class)
        def bus = bus()
        bus.setExecutor(executor)

        when:
        bus.send(new FakeMessage())

        then:
        1 * executor.execute(!null)
    }


    def "encapsulate commands in synchronizations"() {
        given:
        def synchro = Mock(SynchronizationBus)
        def bus = busWith(synchro)
        def command = new FakeMessage()

        when:
        bus.send(command)

        then:
        1 * synchro.beforeExecution(command)
        then:
        1 * synchro.afterExecution()
    }


    def "on error still call synchronization"() {
        given:
        def handler = new FakeCommandCaptor()
        handler.returnException()
        def synchronizationBus = Mock(SynchronizationBus)
        def bus = busWith(handler, synchronizationBus)

        when:
        bus.send new FakeMessage()

        then:
        1 * synchronizationBus.onError()
        then:
        1 * synchronizationBus.atLast()
    }


    def "return the result of a command"() {
        given:
        def handler = new FakeCommandCaptor()
        def bus = busWith(handler)

        when:
        final ListenableFuture<ExecutionResult<String>> promise = bus.send(new FakeMessage())

        then:
        promise != null
        final ExecutionResult<String> response = Futures.getUnchecked(promise)
        response.isSuccess()
        response.data() == "42"
    }

    def "can return directly the result"() {
        given:
        def handler = new FakeCommandCaptor()
        def bus = busWith(handler)

        when:
        def result = bus.sendAndWaitForResponse(new FakeMessage())

        then:
        result != null
    }

    def "return a result on error"() {
        setup:
        def handler = new FakeCommandCaptor()
        handler.returnException();
        def bus = busWith(handler);

        when:
        final ListenableFuture<ExecutionResult<String>> promise = bus.send(new FakeMessage())

        then:
        promise != null
        final ExecutionResult<String> response = Futures.getUnchecked(promise)
        response != null
        response.isError()
        response.error() instanceof RuntimeException
        response.error().message == "This is an error"
    }

    def "return a error if no handler"() {
        given:
        def bus = aEmptyBus()

        when:
        def promise = bus.send(new FakeMessage())

        then:
        promise != null
        def executionResult = promise.get()
        executionResult.isError()
        executionResult.error() instanceof BusError
    }

    def "can execute multiple handlers"() {
        given:
        def captors = [new FakeCommandCaptor(), new FakeCommandCaptor()]
        def bus = new AsynchronousBus(Sets.newHashSet(), captors) {}
        bus.setExecutor(executor())
        def message = new FakeMessage()

        when:
        bus.send(message)

        then:
        captors[0].commandReceived == message
        captors[1].commandReceived == message
    }

    def aEmptyBus() {
        new AsynchronousBus(Sets.newHashSet(), Sets.newHashSet()) {}
    }

    private AsynchronousBus bus() {
        new AsynchronousBus(Sets.newHashSet(mock(SynchronizationBus.class)), Sets.newHashSet(new FakeCommandCaptor())) {
        };
    }

    private AsynchronousBus busWith(FakeCommandCaptor handler, SynchronizationBus synchronizationBus) {
        final AsynchronousBus bus = new AsynchronousBus(Sets.newHashSet(synchronizationBus), Sets.newHashSet(handler)) {

        }
        bus.setExecutor(executor())
        return bus;
    }

    private static ListeningExecutorService executor() {
        return MoreExecutors.sameThreadExecutor()
    }

    private AsynchronousBus busWith(SynchronizationBus synchro) {
        return busWith(aHandler(), synchro)
    }

    private AsynchronousBus busWith(FakeCommandCaptor handler) {
        busWith(handler, mock(SynchronizationBus.class))
    }

    private FakeCommandCaptor aHandler() {
        new FakeCommandCaptor()
    }

    private class FakeMessage implements Message<String> {

        private FakeMessage() {
        }


    }

    private class FakeCommandCaptor implements MessageCaptor<FakeMessage, String> {

        @Override
        public String execute(FakeMessage command) {
            commandReceived = command;
            if (exception) {
                throw new RuntimeException("This is an error");
            }
            return "42";
        }

        public void returnException() {
            this.exception = true;
        }

        private FakeMessage commandReceived;
        private boolean exception;
    }
}
