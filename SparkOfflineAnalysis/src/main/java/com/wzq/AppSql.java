package com.wzq;

import com.alibaba.fastjson.JSON;
import com.wzq.entity.AppEvent;
import com.wzq.entity.AppType;
import com.wzq.util.HbaseUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
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
    JavaRDD<AppEvent> map =
        appDataOneDay
            .javaRDD()
            .map(
                new Function<Row, AppEvent>() {
                  @Override
                  public AppEvent call(Row v1) throws Exception {
                    return new AppEvent();
                  }
                });
    HashMap<String, Long> globalAppCount =
        parallelize
            .groupBy(
                new Function<AppEvent, String>() {
                  @Override
                  public String call(AppEvent appPerPeople) throws Exception {
                    return appPerPeople.getPhone();
                  }
                })
            .mapValues(
                new Function<Iterable<AppEvent>, Iterable<AppEvent>>() {
                  @Override
                  public Iterable<AppEvent> call(Iterable<AppEvent> v1) throws Exception {
                    ArrayList<AppEvent> appEvents1 = new ArrayList<>();
                    v1.iterator().forEachRemaining(appEvents1::add);
                    String phoneRow = appEvents1.get(0).getPhone();

                    // 设备统计近100次内使用最多系统
                    String deviceUseMapString =
                        HbaseUtil.getData(tableName, phoneRow, "behavior", "deviceUse");
                    List<String> deviceUseList = JSON.parseObject(deviceUseMapString, List.class);
                    if (deviceUseList == null) deviceUseList = new LinkedList<>();
                    for (AppEvent appEvent : appEvents1) {
                      if (deviceUseList.size() < 100) {
                        String newDeivce = appEvent.getSystem();
                        deviceUseList.add(newDeivce);
                      } else {
                        deviceUseList.remove(0);
                        String newDeivce = appEvent.getSystem();
                        deviceUseList.add(newDeivce);
                      }
                    }
                    HashMap<String, Long> conutMap = new HashMap<>();
                    for (String s : deviceUseList) {
                      Long orDefault = conutMap.getOrDefault(s, 0L);
                      conutMap.put(s, ++orDefault);
                    }
                    String frequencyDevice = getFrequencyLocation(conutMap);
                    String data =
                        HbaseUtil.getData(tableName, phoneRow, "baseInfo", "useDeviceUsually");
                    if (StringUtils.isBlank(data) || !frequencyDevice.equals(data)) {
                      HbaseUtil.putData(tableName, phoneRow, "baseInfo", new HashMap<>());
                    }
                    // 至此更新完常用设备

                    return v1;
                  }

                  public String getFrequencyLocation(Map<String, Long> map) {
                    return map.entrySet().stream()
                        .sorted(Entry.<String, Long>comparingByValue().reversed()) // reversed不生效
                        .limit(1)
                        .map(Entry::getKey)
                        .findFirst()
                        .get();
                  }
                })
            .mapValues(
                (Function<Iterable<AppEvent>, Map<String, Long>>)
                    v1 -> {
                      ArrayList<AppEvent> appEvents1 = new ArrayList<>();
                      v1.iterator().forEachRemaining(appEvents1::add);
                      String phoneRow = appEvents1.get(0).getPhone();
                      // 提取各APP与数量关系
                      String globalAppString =
                          HbaseUtil.getData(tableName, phoneRow, "behavior", "globalApp");
                      Map<String, Long> globalApp = JSON.parseObject(globalAppString, Map.class);
                      // 合并
                      for (AppEvent appEvent : appEvents1) {
                        String appName = appEvent.getAppId();
                        Long orDefault = globalApp.getOrDefault(appName, 0L);
                        globalApp.put(appName, orDefault);
                      }
                      // 取前10
                      Stream<Entry<String, Long>> sorted = globalApp.entrySet().stream().sorted();

                      // 饼图
                      HashMap<String, Long> bingMap = new HashMap<>();

                      for (Entry<String, Long> stringLongEntry : globalApp.entrySet()) {
                        String type = AppType.getType(stringLongEntry.getKey());
                        Long orDefault = bingMap.getOrDefault(type, 0L);
                        bingMap.put(type, orDefault + stringLongEntry.getValue());
                      }

                      // 更新前10结果 与全局数据
                      HashMap<String, String> result = new HashMap<>();
                      result.put("top10AppHistory", JSON.toJSONString(new HashMap<>()));
                      result.put("globalApp", JSON.toJSONString(globalApp));
                      // 更新饼图
                      result.put("bing", JSON.toJSONString(bingMap));
                      HbaseUtil.putData(tableName, phoneRow, "behavior", result);
                      // 今日新增数据量
                      return globalApp;
                    })
            .aggregate(
                new HashMap<String, Long>(),
                new Function2<
                    HashMap<String, Long>,
                    Tuple2<String, Map<String, Long>>,
                    HashMap<String, Long>>() {
                  @Override
                  public HashMap<String, Long> call(
                      HashMap<String, Long> v1, Tuple2<String, Map<String, Long>> v2)
                      throws Exception {
                    return combineMap(v1, v2._2);
                  }
                },
                new Function2<
                    HashMap<String, Long>, HashMap<String, Long>, HashMap<String, Long>>() {
                  @Override
                  public HashMap<String, Long> call(
                      HashMap<String, Long> v1, HashMap<String, Long> v2) throws Exception {
                    return combineMap(v1, v2);
                  }
                });

    // APP使用数据和APP类型使用数据
    String appHistoryDataCount = HbaseUtil.getData("globalTable", "", "", "appHistoryDataCount");
    Map<String, String> appHistoryCount = JSON.parseObject(appHistoryDataCount, Map.class);
    if (appHistoryCount == null) appHistoryCount = new HashMap<>();
    appHistoryCount.put(new Date().toString(), String.valueOf(count));
    HbaseUtil.putData("", "", "", appHistoryCount);

    globalAppCount.size(); // 取前10，总体分类
  }

  public static HashMap<String, Long> combineMap(HashMap<String, Long> m1, Map<String, Long> m2) {
    for (String m2Key : m2.keySet()) {
      Long orDefault = m1.getOrDefault(m2Key, 0L);
      if (orDefault == 0L) {
        m1.put(m2Key, m2.get(m2Key));
      }
    }
    return m1;
  }
}
