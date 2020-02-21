package com.wzq;

import com.alibaba.fastjson.JSON;
import com.wzq.entity.JizhanLocationTable;
import com.wzq.entity.LocationData;
import com.wzq.util.HbaseUtil;
import com.wzq.util.LocationUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

public class LocationSql {

  // 用户位置计算
  // 计算用户工作地，居住地（白天，晚上） ok
  // 计算用户的常去位置（按从   大到小统计Top5，按饼图进行区域分类）
  // 近一天所去位置 ok
  public static void main(String[] args) throws Exception {
    String tableName = "";
    String behaviorColumn = "behavior";
    JavaSparkContext sc = new JavaSparkContext("local[6]", "asd", new SparkConf());
    sc.setLogLevel("ERROR");
    ArrayList<LocationData> LocationEvents = new ArrayList<LocationData>();
    LocationEvents.add(new LocationData("13264378897", "iei", "31_24", "1342322344", "1"));
    LocationEvents.add(new LocationData("13264378897", "iei", "21_3", "1321245643", "0"));
    LocationEvents.add(new LocationData("13264378897", "iei", "31_24", "1321245644", "1"));
    LocationEvents.add(new LocationData("13264378900", "iei", "21_3", "1321245643", "0"));
    LocationEvents.add(new LocationData("13264378900", "iei", "31_24", "132124561243", "1"));

    // 获取
    SparkSession sparkSession = SparkSession.builder().enableHiveSupport().getOrCreate();
    Dataset<Row> localtionJizhan = sparkSession.sql("select localtionJizhan");
    List<JizhanLocationTable> rows =
        sparkSession.sql("").collectAsList().stream()
            .map(
                new java.util.function.Function<Row, JizhanLocationTable>() {
                  @Override
                  public JizhanLocationTable apply(Row row) {
                    return new JizhanLocationTable();
                  }
                })
            .collect(Collectors.toList());
    Broadcast<List<JizhanLocationTable>> broadcast = sc.broadcast(rows);
    JavaRDD<LocationData> parallelize = sc.parallelize(LocationEvents);
    // 更新单人数据
    long count =
        parallelize
            .groupBy(
                new Function<LocationData, String>() {
                  @Override
                  public String call(LocationData resultLocationEvent) throws Exception {
                    return resultLocationEvent.getPhone();
                  }
                })
            // 每个人的每天的位置数据
            .mapValues(
                new Function<Iterable<LocationData>, Map<String, Map<String, Long>>>() {
                  @Override
                  public Map<String, Map<String, Long>> call(Iterable<LocationData> locationsData)
                      throws Exception {
                    // 当天该用户所有数据
                    ArrayList<LocationData> locationEvents = new ArrayList<>();
                    locationsData.iterator().forEachRemaining(locationEvents::add);
                    String phoneRow = locationEvents.get(0).getPhone();
                    // 昨天去过的地方
                    HashSet<String> yesterdayLocation = new HashSet<>();
                    locationEvents.stream()
                        .map(LocationData::getLocation)
                        .forEach(yesterdayLocation::add);
                    String yes = JSON.toJSONString(yesterdayLocation);
                    HbaseUtil.putData(
                        tableName, phoneRow, behaviorColumn, getResultlMap("yesterday", yes));
                    // 每个人之前的所有白天数据和夜晚数据
                    String day = HbaseUtil.getData(tableName, phoneRow, behaviorColumn, "day");
                    String night = HbaseUtil.getData(tableName, phoneRow, behaviorColumn, "night");
                    Map<String, Long> dayMap = JSON.parseObject(day, Map.class);
                    Map<String, Long> nightMap = JSON.parseObject(night, Map.class);
                    if (dayMap == null) {
                      dayMap = new HashMap<>();
                    }
                    if (nightMap == null) {
                      nightMap = new HashMap<>();
                    }
                    // 更新白天数据和夜晚数据，计算结果中的工作地和居住地
                    for (LocationData locationEvent : locationEvents) {
                      String dayOrNight = locationEvent.getDayOrNight();
                      String station = locationEvent.getLocation();
                      if (dayOrNight.equals("1")) {
                        dayMap.put(station, dayMap.getOrDefault(station, 0L) + 1);
                      } else {
                        nightMap.put(station, dayMap.getOrDefault(station, 0L) + 1);
                      }
                    }
                    // 更新居住地和工作地点
                    String workSpace =
                        HbaseUtil.getData(tableName, phoneRow, "baseInfo", "workSpace");
                    String liveSpace =
                        HbaseUtil.getData(tableName, phoneRow, "baseInfo", "liveSpace");
                    String workInDay = getFrequencyLocation(dayMap);
                    String liveInNight = getFrequencyLocation(nightMap);
                    if (!workInDay.equals(workSpace) || StringUtils.isBlank(workInDay)) {
                      // 更新 putData
                      HbaseUtil.putData(
                          tableName, phoneRow, "baseInfo", getResultlMap("workSpace", workSpace));
                    }
                    if (!liveInNight.equals(liveSpace) || StringUtils.isBlank(liveInNight)) {
                      // 更新 putData
                      HbaseUtil.putData(
                          tableName, phoneRow, "baseInfo", getResultlMap("liveSpace", workSpace));
                    }
                    // 写入白天夜晚数据到HBase
                    String dayMapUpdateString = JSON.toJSONString(dayMap);
                    String nightMapUpdateString = JSON.toJSONString(nightMap);
                    HashMap<String, String> updateMap = new HashMap<>();
                    updateMap.put("day", dayMapUpdateString);
                    updateMap.put("night", nightMapUpdateString);
                    HbaseUtil.putData(tableName, phoneRow, behaviorColumn, updateMap);

                    Map<String, Long> resultMap = new HashMap<>();
                    resultMap.putAll(dayMap);
                    resultMap.putAll(nightMap);
                    Map<String, Map<String, Long>> map = new HashMap<>();
                    map.put(phoneRow, resultMap);
                    return map;
                  }

                  public Map<String, String> getResultlMap(String key, String value) {
                    HashMap<String, String> updateMap = new HashMap<>();
                    updateMap.put(key, value);
                    return updateMap;
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
                new Function<Map<String, Map<String, Long>>, Long>() {
                  @Override
                  public Long call(Map<String, Map<String, Long>> maps) throws Exception {
                    String rowKey = maps.keySet().iterator().next();
                    // 整体Map
                    Map<String, Long> allMap = maps.get(rowKey);

                    List<String> topTenMap = getTopTenLocation(allMap);
                    HashMap<String, String> recentMap = new HashMap<>();
                    recentMap.put("recent10Location", JSON.toJSONString(topTenMap));
                    // 写入最近累计常用地点
                    HbaseUtil.putData(tableName, rowKey, behaviorColumn, recentMap);
                    // 饼图
                    Map<String, Long> bingTu = LocationUtil.areaSplit(allMap, broadcast.value());
                    String bingtuString = JSON.toJSONString(bingTu);
                    HashMap<String, String> bingtuData = new HashMap<>();
                    bingtuData.put("bingtu", bingtuString);
                    HbaseUtil.putData(tableName, rowKey, behaviorColumn, bingtuData);
                    return (long) allMap.size();
                  }

                  public List<String> getTopTenLocation(Map<String, Long> map) {
                    List<String> topTenLocation = new ArrayList<>();
                    map.entrySet().stream()
                        .sorted(Entry.<String, Long>comparingByValue().reversed()) // reversed不生效
                        .limit(10)
                        .map(Entry::getKey)
                        .forEach(topTenLocation::add);
                    return topTenLocation;
                  }
                })
            .aggregate(
                0L,
                new Function2<Long, Tuple2<String, Long>, Long>() {
                  @Override
                  public Long call(Long v1, Tuple2<String, Long> v2) throws Exception {
                    return v1 + v2._2;
                  }
                },
                new Function2<Long, Long, Long>() {
                  @Override
                  public Long call(Long v1, Long v2) throws Exception {
                    return v1 + v2;
                  }
                });

    String historyDataCount = HbaseUtil.getData("", "", "", "");
    Map<String, String> historyCount = JSON.parseObject(historyDataCount, Map.class);
    if (historyCount == null) historyCount = new HashMap<>();
    historyCount.put(new Date().toString(), String.valueOf(count));
    HbaseUtil.putData("", "", "", historyCount);
  }
}
