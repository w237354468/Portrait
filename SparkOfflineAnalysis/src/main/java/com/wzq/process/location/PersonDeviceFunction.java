package com.wzq.process.location;

import com.alibaba.fastjson.JSON;
import com.wzq.entity.app.AppEvent;
import com.wzq.util.HbaseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.function.Function;

public class PersonDeviceFunction implements Function<Iterable<AppEvent>,Iterable<AppEvent>> {

  String tableName;

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
}
