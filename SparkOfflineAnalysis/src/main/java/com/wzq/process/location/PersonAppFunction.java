package com.wzq.process.location;

import com.alibaba.fastjson.JSON;
import com.wzq.entity.app.AppEvent;
import com.wzq.entity.app.AppType;
import com.wzq.util.HbaseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.apache.spark.api.java.function.Function;

public class PersonAppFunction implements Function<Iterable<AppEvent>,Map<String ,Long>> {

  String tableName;
  @Override
  public Map<String ,Long> call(Iterable<AppEvent> v1) throws Exception {
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
}
}
