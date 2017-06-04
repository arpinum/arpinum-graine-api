package arpinum.saga

import arpinum.command.Command
import arpinum.command.CommandBus
import arpinum.command.CommandHandler
import arpinum.ddd.event.Event
import arpinum.infrastructure.bus.command.CommandBusAsynchronous
import com.google.common.util.concurrent.MoreExecutors
import io.vavr.Tuple
import io.vavr.Tuple2
import io.vavr.collection.List
import io.vavr.collection.Seq
import io.vavr.concurrent.Future
import spock.lang.Specification

class SagaMiddlewareTest extends Specification {


    def "pass through if not a saga"() {
        given:
        def handler = new FakeCommandHandler()
        def bus = new CommandBusAsynchronous([new SagaMiddleware([] as Set)] as Set, [handler] as Set, MoreExecutors.newDirectExecutorService())

        when:
        bus.send(new FakeMessage())

        then:
        handler.message != null
    }

    def "invokes saga when knows"() {
        given:
        def handler = new FakeCommandHandler()
        def sagaHandler = new FakeSagaHandler()
        def bus = new CommandBusAsynchronous([new SagaMiddleware([sagaHandler] as Set)] as Set, [handler] as Set, MoreExecutors.newDirectExecutorService())
        def saga = new FakeSaga()

        when:
        def result = bus.send(saga)

        then:
        handler.message == null
        sagaHandler.saga == saga
        result.get() == "ok"
    }

    private class FakeCommandHandler implements CommandHandler<FakeMessage, String> {

        def message

        @Override
        Tuple2<String, Seq<Event<?>>> execute(FakeMessage fakeMessage) {
            message = fakeMessage
            Tuple.of("response", List.empty())
        }
    }

    private class FakeMessage implements Command<String> {

        private FakeMessage() {
        }
    }

    private class FakeSaga implements Saga<String> {

    }

    private class FakeSagaHandler implements SagaHandler<String, FakeSaga> {

        FakeSaga saga

        @Override
        Future<String> run(CommandBus bus, FakeSaga saga) {
            this.saga = saga
            Future.successful("ok")
        }
    }
}
