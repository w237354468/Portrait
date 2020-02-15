package com.wzq.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class MapUtils {

  public static String  getMaxInMap(Map<String, Long> map) {
    if (map.isEmpty()) return null;
    TreeMap<Long, String> treeMap =
        new TreeMap<Long, String>(
            new Comparator<Long>() {
              public int compare(Long o1, Long o2) {
                return o2.compareTo(o1); // 大到小
              }
            });
    // TreeMap根据Key进行排序
    Set<Entry<String, Long>> entries = map.entrySet();
    for (Entry<String, Long> entry : entries) {
      String key = entry.getKey();
      Long value = entry.getValue();
      treeMap.put(value, key);
    }
    return treeMap.get(treeMap.firstKey());
  }
}
