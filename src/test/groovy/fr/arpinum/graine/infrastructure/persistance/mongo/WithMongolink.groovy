package fr.arpinum.graine.infrastructure.persistance.mongo

import com.gmongo.GMongo
import com.mongodb.DB
import com.mongodb.DBCollection
import org.junit.rules.ExternalResource
import org.mongolink.MongoSession
import org.mongolink.test.MongolinkRule

public class WithMongolink extends ExternalResource {

    public static WithMongolink withPackage(String nomPackage) {
        final WithMongolink résultat = new WithMongolink()
        résultat.mongolink = MongolinkRule.withPackage(nomPackage)
        return résultat
    }

    private WithMongolink() {
    }

    @Override
    protected void before() throws Throwable {
        mongolink.before()
        gMongo = new GMongo(mongolink.getCurrentSession().getDb().getMongo())
    }

    @Override
    protected void after() {
        mongolink.after()
    }


    public void cleanSession() {
        mongolink.cleanSession()
    }

    public MongoSession currentSession() {
        return mongolink.getCurrentSession()
    }

    public DB db() {
        return gMongo.getDB("test")
    }

    public DBCollection collection(String collection) {
        return db().getCollection(collection)
    }

    private MongolinkRule mongolink
    private GMongo gMongo
}
