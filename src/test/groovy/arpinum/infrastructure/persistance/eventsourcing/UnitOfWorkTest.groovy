package arpinum.infrastructure.persistance.eventsourcing

import arpinum.ddd.BaseAggregate
import arpinum.ddd.evenement.Event
import arpinum.ddd.evenement.EventBus
import spock.lang.Specification

import java.util.function.Supplier


class UnitOfWorkTest extends Specification {

    def store = new EventStoreMemory()
    def bus = Mock(EventBus)
    def unitOfWork = new UnitOfWork(store, bus)

    def "invokes loader first time"() {
        given:
        def aggregate = new FakeAggregate("id")
        def loader = Mock(Supplier)

        when:
        def result = unitOfWork.get("test", FakeAggregate, loader)

        then:
        result == aggregate
        1 * loader.get() >> aggregate
    }

    def "do not invoke loader twice"() {
        given:
        def aggregate = new FakeAggregate("id")
        def loader = Mock(Supplier)
        unitOfWork.get("id", FakeAggregate, {aggregate})

        when:
        def result = unitOfWork.get("id", FakeAggregate, loader)

        then:
        result == aggregate
        0 * loader.get()
    }

    def "aggregate can be added manually"() {
        given:
        def aggregate = new FakeAggregate("id")

        when:
        unitOfWork.add(aggregate)

        then:
        unitOfWork.get("id", FakeAggregate, {}) == aggregate
    }

    def "flushes on error"() {
        given:
        unitOfWork.add(new FakeAggregate('id'))

        when:
        unitOfWork.onError()

        then:
        unitOfWork.currentUnit.get().isEmpty()
    }

    def "flushes at last"() {
        given:
        unitOfWork.add(new FakeAggregate('id'))

        when:
        unitOfWork.atLast()

        then:
        unitOfWork.currentUnit.get().isEmpty()
    }

    def "saves to event store and publish events"() {
        given:
        unitOfWork.add(new FakeAggregate('id'))

        when:
        unitOfWork.afterExecution()

        then:
        store.allOf(FakeAggregate).count() == 1
        1 * bus.publish(_)
    }

    def "marks for deletion and delete on flush"() {
        given:
        def aggregate = new FakeAggregate('id')
        unitOfWork.add(aggregate)
        store.save(new FakeEvent('id'))

        when:
        unitOfWork.delete(aggregate)
        unitOfWork.afterExecution()

        then:
        store.allOf(FakeAggregate).count() == 0
    }

    def "waits for flush to delete"() {
        given:
        def aggregate = new FakeAggregate('id')
        unitOfWork.add(aggregate)
        store.save(new FakeEvent('id'))

        when:
        unitOfWork.delete(aggregate)

        then:
        store.allOf(FakeAggregate).count() == 1
    }

    static class FakeAggregate extends BaseAggregate<String> {
        FakeAggregate(String s) {
            super(s)
            pushEvent(new FakeEvent(s))
        }
    }

    static class FakeEvent extends Event<FakeAggregate> {
        FakeEvent() {
        }

        FakeEvent(Object targetId) {
            super(targetId)
        }
    }
}
