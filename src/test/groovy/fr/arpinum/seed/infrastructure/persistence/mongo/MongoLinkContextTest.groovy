package fr.arpinum.seed.infrastructure.persistence.mongo

import org.mongolink.MongoSession
import org.mongolink.MongoSessionManager
import spock.lang.Specification

class MongoLinkContextTest extends Specification {

    def sessionManager = Mock(MongoSessionManager)
    def session = Mock(MongoSession)

    def "start a session when a command start"() {
        given:
        def context = new MongoLinkContext(sessionManager)
        sessionManager.createSession() >> session

        when:
        context.beforeExecution(null)

        then:
        1 * session.start()
        context.currentSession() == session
    }

    def "finally stop session"() {
        given:
        def context = new MongoLinkContext(sessionManager)
        sessionManager.createSession() >> session

        when:
        context.atLast()

        then:
        1 * session.stop()
    }

    def "clear session if error"() {
        given:
        def context = new MongoLinkContext(sessionManager)
        sessionManager.createSession() >> session

        when:
        context.onError()

        then:
        1 * session.clear()
    }


}
