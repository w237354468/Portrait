package com.wzq.task;

import com.wzq.entity.YearBase;
import com.wzq.map.YearBaseMap;
import com.wzq.reduce.YearBaseReduce;
import com.wzq.util.MonGoUtil;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.LocalEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

public class YearBaseTask {

  public static void main(String[] args) {
    ParameterTool parameter = ParameterTool.fromArgs(args);
    LocalEnvironment env = ExecutionEnvironment.createLocalEnvironment();
    env.getConfig().setGlobalJobParameters(parameter);

    // 统计相同年代人数
    DataSource<String> text = env.readTextFile("input");
    DataSet<YearBase> mapResult = text.map(new YearBaseMap());
    DataSet<YearBase> reduceResult = mapResult.groupBy("groupFieldId").reduce(new YearBaseReduce());
    // 更新或删除总人数
    try {
      List<YearBase> resultList = reduceResult.collect();
      for (YearBase yearBase : resultList) {
        String ageZone = yearBase.getYearType();
        long count = yearBase.getCount();
        Document doc = MonGoUtil.findoneby("yearbasestatics", "wzqPortrait", ageZone);

        if (doc == null) {
          doc = new Document();
          doc.put("info", ageZone);
          doc.put("count", count);

        } else {
          Long countpre = doc.getLong("count");
          Long countTotal = count + countpre;
          doc.put("count", countTotal);
        }
        MonGoUtil.saverupdateMongo("yearbasestatics", "wzqPortrait", doc);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      env.execute("yearbase");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
