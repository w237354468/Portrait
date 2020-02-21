package com.wzq;

import com.alibaba.fastjson.JSON;
import com.wzq.entity.app.AppEvent;
import com.wzq.process.location.PersonAppFunction;
import com.wzq.process.location.PersonDeviceFunction;
import com.wzq.process.location.RowToAppEventMap;
import com.wzq.util.HbaseUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

public class AppSql {
  // 单个用户常用系统标签
  // 单个用户的APP偏好 前10APP   类别饼图
  // 各类APP的使用人数 各个APP的使用人数
  // 总数据量变化
  // APP 表，APP代号，名称，类别，描述，所属公司
  public static void main(String[] args) throws Exception {
    String tableName = "";
    String columnName = "";
    JavaSparkContext sc = new JavaSparkContext("local[6]", "asd", new SparkConf());
    //    SparkSession sparkSession = SparkSession.builder().enableHiveSupport().getOrCreate();
    sc.setLogLevel("ERROR");
    ArrayList<AppEvent> appEvents = new ArrayList<AppEvent>();
    appEvents.add(new AppEvent("13264378897", "12312", "Android5.5", "1", "12312412"));
    appEvents.add(new AppEvent("13264378898", "12312", "IOS13", "3", "21412412"));
    appEvents.add(new AppEvent("13264378899", "12312", "Android5.5", "4", "125112512"));
    appEvents.add(new AppEvent("13264378900", "12312", "Android5.5", "7", "12121421"));
    appEvents.add(new AppEvent("13264378901", "12312", "IOS13", "9", "112321323"));

    JavaRDD<AppEvent> parallelize = sc.parallelize(appEvents);

    SparkSession ss = SparkSession.builder().enableHiveSupport().getOrCreate();
    Dataset<Row> appDataOneDay = ss.sql("select * from xxx where partition=''");
    long count = appDataOneDay.count();
    JavaRDD<AppEvent> map = appDataOneDay.javaRDD().map(new RowToAppEventMap());
    HashMap<String, Long> globalAppCount =
        parallelize
            .groupBy((Function<AppEvent, String>) AppEvent::getPhone)
            .mapValues(new PersonDeviceFunction())
            .mapValues(new PersonAppFunction())
            .aggregate(
                new HashMap<>(),
                (Function2<
                        HashMap<String, Long>,
                        Tuple2<String, Map<String, Long>>,
                        HashMap<String, Long>>)
                    (v1, v2) -> combineMap(v1, v2._2),
                (Function2<HashMap<String, Long>, HashMap<String, Long>, HashMap<String, Long>>)
                    AppSql::combineMap);

    // APP使用数据和APP类型使用数据
    String appHistoryDataCount = HbaseUtil.getData("globalTable", "", "", "appHistoryDataCount");
    Map<String, String> appHistoryCount = JSON.parseObject(appHistoryDataCount, Map.class);
    if (appHistoryCount == null) appHistoryCount = new HashMap<>();
    appHistoryCount.put(new Date().toString(), String.valueOf(count));
    HbaseUtil.putData("", "", "", appHistoryCount);

    globalAppCount.size(); // 取前10，饼图分类
  }

  public static HashMap<String, Long> combineMap(HashMap<String, Long> m1, Map<String, Long> m2) {
    for (String m2Key : m2.keySet()) {
      Long orDefault = m1.getOrDefault(m2Key, 0L);
      if (orDefault == 0L) {
        m1.put(m2Key, m2.get(m2Key));
      } else {
        m1.put(m2Key, m2.get(m2Key) + m1.get(m2Key));
      }
    }
    return m1;
  }
}
