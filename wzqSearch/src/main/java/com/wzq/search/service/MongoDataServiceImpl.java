package com.wzq.search.service;

import com.alibaba.fastjson.JSONObject;
//import com.mongodb.client.AggregateIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
import com.wzq.entity.AnalyResult;
import com.wzq.search.base.BaseMongo;
//import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// MongoUtil 访问Mongolian中的数据，YearBase在Mongo存储了全局的数据
@Service
public class MongoDataServiceImpl extends BaseMongo {

  // mongo命令
  // $match where
  // $group group by
  // $match having
  // $project select
  // $sort order by
  // $limit limit
  // $sum sum()
  // $sum $sortByCount count()
  // $lookup join

  /**
   * @param tableName 具体哪个维度的数据
   * @return 返回的是维度+个数
   */
  public List<AnalyResult> listYearBaseInfoBy(String tableName) {

    ArrayList<AnalyResult> analyResults = new ArrayList<>();
//    String databaseName = "wzqPortrait";
//    MongoDatabase database = mongoClient.getDatabase(databaseName);
//      MongoCollection<Document> collection = database.getCollection(tableName);
//    long now = System.currentTimeMillis();
//
//    // 在MongoDb中主键是 _id ，如果用户不主动为其分配，MongoDb会自动生成
//    Document groupField = new Document();
//    Document idFields = new Document();
//    idFields.put("info", "$info");
//    groupField.put("_id", idFields);
//    idFields.put("count", new Document("$sum", "$count"));
//
//    Document group = new Document("group", groupField);
//    Document projectFields = new Document();
//    projectFields.put("_id", false);
//    projectFields.put("info", "$_id.info");
//    projectFields.put("count", true);
//    Document project = new Document("$project", projectFields);
//    AggregateIterable<Document> iterator =
//        collection.aggregate((List<Document>) Arrays.asList(group, project));
//
//    for (Document document : iterator) {
//      String jsonString = JSONObject.toJSONString(document);
//      AnalyResult analyResult = JSONObject.parseObject(jsonString, AnalyResult.class);
//      analyResults.add(analyResult);
//    }
    return analyResults;
  }
}
