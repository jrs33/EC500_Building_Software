package org.videoMaker.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class MongoLogger {
    public static synchronized void logInMongo(DBCollection collection, BasicDBObject object) throws Exception {
        collection.insert(object);
    }
}
