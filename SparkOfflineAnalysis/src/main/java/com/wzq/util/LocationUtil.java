package com.wzq.util;

import com.alibaba.fastjson.JSON;
import com.wzq.entity.location.JizhanLocationTable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationUtil {

  // 读入两张表
  // lac ci  code  type 区域 北京市丰台区六里桥基站
  public static Map<String, Long> areaSplit(
      Map<String, Long> location, List<JizhanLocationTable> jizhanList) throws Exception {

    String area = HbaseUtil.getData("", "", "", "Area");
    Map<String, Long> areaMap = JSON.parseObject(area, Map.class);
    if (areaMap == null) areaMap = new HashMap<>();
    for (String key : location.keySet()) {
      for (JizhanLocationTable jizhanLocationTable : jizhanList) {
        if (key.equals(jizhanLocationTable.getLac_ci_code())) {
          Long aLong = areaMap.getOrDefault(key, 0L);
          areaMap.put(key, ++aLong);
        }
      }
    }
    return areaMap;
  }
}
