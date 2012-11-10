package com.ee.beaver.domain.operation;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

class UpdateDocument implements Operation {

  private final Mongo mongo;

  public UpdateDocument(final Mongo mongo) {
    this.mongo = mongo;
  }

  @Override
  public void execute(final DBObject document) {
    final String ns = (String) document.get("ns");
    int index = ns.indexOf(".");
    if (index != -1) {
      String dbName = ns.substring(0, index);
      String collectionName = ns.substring(index + 1, ns.length());
      DBObject id = (DBObject) JSON.parse(document.get("o2").toString());
      DBObject uSpec = (DBObject) JSON.parse(document.get("o").toString());
      mongo.getDB(dbName).getCollection(collectionName).update(id, uSpec);
    }
  }
}
