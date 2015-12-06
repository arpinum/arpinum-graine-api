package fr.arpinum.graine.infrastructure.persistance.mongo

import org.mongolink.MongoSession
import org.mongolink.MongoSessionManager
import spock.lang.Specification

class MongoLinkContextTest extends Specification {

    def sessionManager = Mock(MongoSessionManager)
    def session = Mock(MongoSession)

    def "démarre une session au début d'une commande"() {
        given:
        def contexte = new MongoLinkContext(sessionManager)
        sessionManager.createSession() >> session

        when:
        contexte.beforeExecution(null)

        then:
        1 * session.start()
        contexte.currentSession() == session
    }

    def "stop la session finalement"() {
        given:
        def contexte = new MongoLinkContext(sessionManager)
        sessionManager.createSession() >> session

        when:
        contexte.atLast()

        then:
        1 * session.stop()
    }

    def "clear la session si erreur"() {
        given:
        def contexte = new MongoLinkContext(sessionManager)
        sessionManager.createSession() >> session

        when:
        contexte.onError()

        then:
        1 * session.clear()
    }


}
