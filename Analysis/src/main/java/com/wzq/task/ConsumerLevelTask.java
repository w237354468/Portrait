package com.wzq.task;

import com.wzq.entity.ConsumptionLevel;
import com.wzq.map.ConsumptionMap;
import com.wzq.reduce.ConsumptionLevelReduce;
import com.wzq.reduce.ConsumptionResultReduce;
import com.wzq.util.MonGoUtil;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.LocalEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

public class ConsumerLevelTask {

  public static void main(String[] args) {
    ParameterTool parameter = ParameterTool.fromArgs(args);
    LocalEnvironment env = ExecutionEnvironment.createLocalEnvironment();
    env.getConfig().setGlobalJobParameters(parameter);

    // 统计相同年代人数
    DataSource<String> text = env.readTextFile("input");
    DataSet<ConsumptionLevel> mapResult = text.map(new ConsumptionMap());
    DataSet<ConsumptionLevel> reduceResult =
        mapResult.groupBy("groupFieldId").reduceGroup(new ConsumptionLevelReduce());
    ReduceOperator<ConsumptionLevel> finalReduce =
        reduceResult.groupBy("groupFieldId").reduce(new ConsumptionResultReduce());
    // 更新或删除总人数
    try {
      List<ConsumptionLevel> resultList = finalReduce.collect();
      for (ConsumptionLevel consumptionLevel : resultList) {
        String levelConsumption = consumptionLevel.getConsumption();
        long count = consumptionLevel.getCount();
        Document doc =
            MonGoUtil.findoneby("consumptionLevelstatics", "youfanPortrait", levelConsumption);

        if (doc == null) {
          doc = new Document();
          doc.put("info", levelConsumption);
          doc.put("count", count);

        } else {
          Long countpre = doc.getLong("count");
          Long countTotal = count + countpre;
          doc.put("count", countTotal);
        }
        MonGoUtil.saverupdateMongo("consumptionLevelstatics", "youfanPortrait", doc);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      env.execute("ConsumerLevelTask");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
