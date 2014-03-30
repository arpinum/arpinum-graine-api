package fr.arpinum.ddd.persistance.mongo

import fr.arpinum.ddd.modele.Entrepot
import fr.arpinum.ddd.modele.Racine
import org.mongolink.MongoSession
import org.mongolink.test.MongolinkRule
import spock.lang.Ignore
import spock.lang.Specification

class EntrepotMongoTest extends Specification {

    def setup() {
        mongolink = MongolinkRule.withPackage("fr.arpinum.ddd.persistance.mongo")
        mongolink.before()
    }

    def cleanup() {
        mongolink.after()
    }

    @Ignore
    def "peut ajouter"() {
        given:
        def entrepot = new EntrepoFausseRacine(mongolink.currentSession)

        when:
        entrepot.ajoute(new FausseRacine("1"))
        mongolink.cleanSession();


        then:
        def racineTrouvé = entrepot.get("1")
        racineTrouvé != null
        racineTrouvé.id == "1"
    }

    def mongolink

    public class FausseRacine implements Racine<String> {

        FausseRacine(String id) {
            this.id = id
        }

        @Override
        String getId() {
            return this.id;
        }

        public String id;
    }

    public class EntrepoFausseRacine extends EntrepotMongo<String, FausseRacine> implements Entrepot<String, FausseRacine> {

        protected EntrepoFausseRacine(MongoSession session) {
            super(session)
        }


    }
}
