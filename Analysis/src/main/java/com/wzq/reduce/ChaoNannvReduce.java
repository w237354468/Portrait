package com.wzq.reduce;

import com.alibaba.fastjson.JSONObject;
import com.wzq.entity.ChaoNanNvInfo;
import com.wzq.util.HbaseUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/** this class is right */
public class ChaoNannvReduce
    implements WindowFunction<ChaoNanNvInfo, ChaoNanNvInfo, String, TimeWindow> {

  @Override
  public void apply(
      String k, TimeWindow window, Iterable<ChaoNanNvInfo> values, Collector<ChaoNanNvInfo> out)
      throws Exception {
    // get all data for one user
    Iterator<ChaoNanNvInfo> iterator = values.iterator();
    // get userId
    String userId = iterator.next().getUserId();
    // aggregate one user's clink count
    Map<String, Long> resultMap = new HashMap<>();
    while (iterator.hasNext()) {
      ChaoNanNvInfo next = iterator.next();
      Long chaoCount = resultMap.getOrDefault(next.getChaoType(), 0L);
      resultMap.put(next.getChaoType(), ++chaoCount);
    }
    String tableName = "userflaginfo";
    String familyName = "userhehavior";
    String column = "chaonannv";
    // get user's history click count and combine
    String data = HbaseUtil.getData(tableName, userId, familyName, column);
    if (StringUtils.isNotBlank(data)) {
      Map<String, Long> reserveMap = JSONObject.parseObject(data, Map.class);
      Set<String> set = reserveMap.keySet();
      for (String key : set) {
        Long pre = reserveMap.getOrDefault(key, 0L);
        resultMap.put(key, ++pre);
      }
    }
    if (!resultMap.isEmpty()) {
      // write combine map to Mongo
      String chaonannvMapString = JSONObject.toJSONString(resultMap);
      HbaseUtil.putData(tableName, userId, familyName, column, chaonannvMapString);
      // static user click man and woman count now then type him
      long chaonan = resultMap.getOrDefault("1", 0L);
      long chaonv = resultMap.getOrDefault("0", 0L);
      String flag = "woman";
      long finalCount = chaonv;
      if (chaonan > chaonv) {
        flag = "man";
        finalCount = chaonan;
      }
      // over min
      if (finalCount > 2000) {
        column = "chaoType";
        // 加上当前数据后的结果
        ChaoNanNvInfo resultChaonannvCurrent = new ChaoNanNvInfo();
        resultChaonannvCurrent.setChaoType(flag);
        resultChaonannvCurrent.setCount(1);
        resultChaonannvCurrent.setGroupField(flag + "==chaonannvAndInfoReduce");
        // 获取之前的结果
        String typePre = HbaseUtil.getData(tableName, userId, familyName, column);
        // 如果不一样，之前的结果在全局结果中减1
        if (StringUtils.isNotBlank(typePre) && !typePre.equals(flag)) {
          ChaoNanNvInfo resultChaonannvPre = new ChaoNanNvInfo();
          resultChaonannvPre.setChaoType(typePre);
          resultChaonannvPre.setCount(-1);
          resultChaonannvPre.setGroupField(typePre + "==chaonannvAndInfoReduce");
          out.collect(resultChaonannvPre);
        }
        // 不管一样不一样，把更新后的数据写入
        HbaseUtil.putData(tableName, userId, familyName, column, flag);
        // 相等的话我觉得此处可以不写出，不是很理解
        out.collect(resultChaonannvCurrent);
      }
    }
  }
}
