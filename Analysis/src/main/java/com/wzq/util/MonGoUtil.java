package com.wzq.util;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MonGoUtil {

  // mongo DB中类似于Collection是表，Document是行，存储的是key,value对
  private static MongoClient mongoClient = new MongoClient("ip", 0000);

  public static Document findoneby(String tableName, String database, String  type) {

    MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
    MongoCollection mongoCollection = mongoDatabase.getCollection(tableName);
    Document doc = new Document();
    doc.put("info", type);
    FindIterable<Document> itera = mongoCollection.find(doc);
    MongoCursor<Document> mongoCursor = itera.iterator();

    if (mongoCursor.hasNext()) {
      return mongoCursor.next();
    } else {
      return null;
    }
  }

  public static void saverupdateMongo(String tableName, String database, Document doc) {
    MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
    MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(tableName);
    if (!doc.containsKey("_id")) {
      ObjectId objectId = new ObjectId();
      doc.put("_id", objectId);
      mongoCollection.insertOne(doc);
      return;
    }
    Document matchDocument = new Document();
    String objectId = doc.get("_id").toString();
    matchDocument.put("_id", new ObjectId(objectId));
    FindIterable<Document> findIterator = mongoCollection.find(matchDocument);
    if (findIterator.iterator().hasNext()) {
      mongoCollection.updateOne(matchDocument, new Document("$set", doc));
      try {
        System.out.println(
            "come into saveorupdatemongo --- update --- " + JSONObject.toJSONString(doc));
      } catch (Exception e) {
        e.printStackTrace();
      }

    } else {
      mongoCollection.insertOne(doc);
      try {
        System.out.println(
            "come into saveorpudatemongo --- insert --- " + JSONObject.toJSONString(doc));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
