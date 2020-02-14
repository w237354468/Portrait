package com.wzq.task;

import com.wzq.entity.CarrierInfo;
import com.wzq.map.CarrierMap;
import com.wzq.reduce.CarrierReduce;
import com.wzq.util.MonGoUtil;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.LocalEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

public class CarrierTask {

  public static void main(String[] args) {
    ParameterTool parameter = ParameterTool.fromArgs(args);
    LocalEnvironment env = ExecutionEnvironment.createLocalEnvironment();
    env.getConfig().setGlobalJobParameters(parameter);

    // 统计相同年代人数
    DataSource<String> text = env.readTextFile("input");
    DataSet<CarrierInfo> mapResult = text.map(new CarrierMap());
    DataSet<CarrierInfo> reduceResult =
        mapResult.groupBy("groupFieldId").reduce(new CarrierReduce());
    // 更新或删除总人数
    try {
      List<CarrierInfo> resultList = reduceResult.collect();
      for (CarrierInfo carrier : resultList) {
        String carrierName = carrier.getCarrier();
        long count = carrier.getCount();
        // 检测Mongo中是否有运营商标签的统计
        Document doc = MonGoUtil.findoneby("carrierstatics", "youfanPortrait", carrierName);

        if (doc == null) {
          doc = new Document();
          doc.put("info", carrierName);
          doc.put("count", count);

        } else {
          Long countpre = doc.getLong("count");
          Long countTotal = count + countpre;
          doc.put("count", countTotal);
        }
        MonGoUtil.saverupdateMongo("carrierstatics", "youfanPortrait", doc);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      env.execute("carrier");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
