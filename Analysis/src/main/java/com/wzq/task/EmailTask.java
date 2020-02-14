package com.wzq.task;

import com.wzq.entity.EmailEntity;
import com.wzq.map.EmailMap;
import com.wzq.reduce.EmailReduce;
import com.wzq.util.MonGoUtil;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.LocalEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

public class EmailTask {
  public static void main(String[] args) {
    ParameterTool parameter = ParameterTool.fromArgs(args);
    LocalEnvironment env = ExecutionEnvironment.createLocalEnvironment();
    env.getConfig().setGlobalJobParameters(parameter);

    // 统计相同年代人数
    DataSource<String> text = env.readTextFile("input");
    DataSet<EmailEntity> mapResult = text.map(new EmailMap());
    DataSet<EmailEntity> reduceResult =
        mapResult.groupBy("groupFieldId").reduce(new EmailReduce());
    // 更新或删除总人数
    try {
      List<EmailEntity> resultList = reduceResult.collect();
      for (EmailEntity emailCountByName : resultList) {
        String emailName = emailCountByName.getEmailType();
        long count = emailCountByName.getCount();
        // 检测Mongo中是否有运营商标签的统计
        Document doc = MonGoUtil.findoneby("emailtatics", "youfanPortrait", emailName);

        if (doc == null) {
          doc = new Document();
          doc.put("info", emailName);
          doc.put("count", count);

        } else {
          Long countpre = doc.getLong("count");
          Long countTotal = count + countpre;
          doc.put("count", countTotal);
        }
        MonGoUtil.saverupdateMongo("emailstatics", "youfanPortrait", doc);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      env.execute("email");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
