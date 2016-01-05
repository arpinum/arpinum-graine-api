package fr.arpinum.seed.model.event

import com.google.common.collect.Sets
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import spock.lang.Specification

class AsyncEventBusTest extends Specification {

    def "doesn't execute immediately the event"() {
        given:
        def captor = new FakeEventCaptor()
        AsyncEventBus bus = busWith(captor)

        when:
        bus.publish(new FakeEvent())

        then:
        !captor.called
    }

    def "run execute after commands"() {
        given:
        def captor = new FakeEventCaptor()
        AsyncEventBus bus = busWith(captor)

        when:
        bus.publish(new FakeEvent())
        bus.afterExecution()

        then:
        captor.called
    }

    def "can execute captor in synchronous"() {
        given:
        AsyncEventBus bus = busWith(new FakeSynchronousEventCaptor())
        def future = Mock(ListenableFuture)
        bus.executor = aExecutorThatReturn(future)

        when:
        bus.publish(new FakeSynchronousEvent())
        bus.afterExecution()

        then:
        0 * future.get()
    }

    private aExecutorThatReturn(future) {
        [submit: {
            return future
        }] as ListeningExecutorService
    }

    private busWith(EventCaptor<? extends Event> captor) {
        def bus = new AsyncEventBus(Sets.newHashSet(), Sets.newHashSet(captor))
        bus.setExecutor(MoreExecutors.sameThreadExecutor())
        return bus
    }

    public static class FakeEvent implements Event {

    }

    public static class FakeEventCaptor implements EventCaptor<FakeEvent> {

        boolean called

        @Override
        void executeEvent(FakeEvent event) {
            called = true
        }
    }

    @Synchronous
    public static class FakeSynchronousEvent implements Event {

    }

    public static class FakeSynchronousEventCaptor implements EventCaptor<FakeSynchronousEvent> {

        boolean called


        @Override
        void executeEvent(FakeSynchronousEvent event) {
            called = true
        }
    }
}
