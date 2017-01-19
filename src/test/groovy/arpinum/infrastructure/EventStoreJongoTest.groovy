package arpinum.infrastructure

import arpinum.ddd.evenement.Event
import org.junit.Rule

import arpinum.infrastructure.persistance.eventsourcing.EventStoreJongo
import arpinum.query.WithJongo
import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

class EventStoreJongoTest extends Specification {

    @Rule
    WithJongo jongo = new WithJongo()

    EventStoreJongo store

    void setup() {
        store = new EventStoreJongo("fake", jongo.jongo())
    }

    def "can persist"() {
        given:
        def entity = new FakeEntity(id: "id")
        def event = new FakeEvent(entity, "stuff")

        when:
        store.save(event)

        then:
        def record = jongo.collection("fake_eventstore").findOne()
        record != null
        record.targetId == "id"
        record.targetType == "FakeEntity"
        record.timestamp != null
        record.someData == "stuff"
        record._class == 'arpinum.infrastructure.EventStoreJongoTest$FakeEvent'
    }

    def "can load"() {
        given:
        jongo.collection('fake_eventstore') << [targetId    : "id"
                                                , targetType: "FakeEntity"
                                                , timestamp : Instant.EPOCH.toEpochMilli()
                                                , someData  : "data"
                                                , _class    : 'arpinum.infrastructure.EventStoreJongoTest$FakeEvent']

        when:
        def events = store.allOf(FakeEntity.class).collect(Collectors.toList())

        then:
        events != null
        events.size == 1
        def event = events[0] as FakeEvent
        event.someData == "data"
        event.timestamp == 0
        event.targetId == "id"
        event.targetType == "FakeEntity"
    }

    def "can load given event type"() {
        given:
        jongo.collection('fake_eventstore') << [targetId    : "id"
                                                , targetType: "FakeEntity"
                                                , timestamp : Instant.EPOCH.toEpochMilli()
                                                , someData  : "data"
                                                , _class    : 'arpinum.infrastructure.EventStoreJongoTest$FakeEvent']

        when:
        def events = store.allOfWithType(FakeEntity, FakeEvent)

        then:
        events != null
        def eventualFirst = events.findFirst()
        eventualFirst.isPresent()
    }

    def "mark as deleted"() {
        given:
        jongo.collection('fake_eventstore') << [targetId    : "id"
                                                , targetType: "FakeEntity"
                                                , timestamp : Instant.EPOCH.toEpochMilli()
                                                , someData  : "data"
                                                , _class    : 'arpinum.infrastructure.EventStoreJongoTest$FakeEvent']

        when:
        store.markAllAsDeleted("id", FakeEntity.class)

        then:
        def record = jongo.collection('fake_eventstore').findOne([targetId: "id"])
        record._deleted == true
    }

    def "do not load delete events"() {
        given:
        jongo.collection('fake_eventstore') << [targetId    : "id"
                                                , targetType: "FakeEntity"
                                                , timestamp : Instant.EPOCH.toEpochMilli()
                                                , someData  : "data"
                                                , _deleted  : true
                                                , _class    : 'arpinum.infrastructure.EventStoreJongoTest$FakeEvent']

        when:
        def events = store.allOf(FakeEntity.class).collect(Collectors.toList())

        then:
        events != null
        events.size == 0
    }

    def "loads ordered by timestamp"() {
        given:
        def first = new FakeEvent(new FakeEntity(id: "id"), "dataFirst")
        store.save(first)
        def second = new FakeEvent(new FakeEntity(id: "id"), "dataSecond")
        store.save(second)

        when:
        def events = store.allOf("id", FakeEntity.class).collect(Collectors.toList())

        then:
        events[0].someData == "dataFirst"
        events[1].someData == "dataSecond"
    }

    def "deals with duration"() {
        given:
        def entity = new FakeEntity()
        store.save(new FakeEventWithDuration(entity, Duration.of(30, ChronoUnit.DAYS)))

        when:
        def result = store.allOf(entity.id, FakeEntity).collect(Collectors.toList())

        then:
        result.size() == 1
    }

    private static class FakeEvent extends Event<FakeEntity> {

        FakeEvent() {

        }

        FakeEvent(FakeEntity entity, String data) {
            super(entity.id)
            someData = data
        }

        String someData
    }

    private static class FakeEventWithDuration extends Event<FakeEntity> {

        FakeEventWithDuration() {
        }

        FakeEventWithDuration(FakeEntity entity, Duration duration) {
            super(entity.id)
            this.duration = duration
        }

        Duration duration
        String someData
    }

    private static class FakeEntity {
        public String id
    }
}
