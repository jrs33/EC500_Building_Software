package org.videoMaker.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public interface LoggedResource {
    void log(DBCollection collection, BasicDBObject object);
}
