package com.wzq.map;

import com.alibaba.fastjson.JSONObject;
import com.wzq.entity.UseTypeInfo;
import com.wzq.kafkause.KafkaEvent;
import com.wzq.log.ScanProductLog;
import com.wzq.util.HbaseUtil;
import com.wzq.utils.MapUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class UserTypeMap implements FlatMapFunction<KafkaEvent, UseTypeInfo> {

  @Override
  public void flatMap(KafkaEvent value, Collector<UseTypeInfo> out) throws Exception {

    // 获取的是每条浏览数据
    String data = value.getWord();
    ScanProductLog scanProductLog = JSONObject.parseObject(data, ScanProductLog.class);

    int userId = scanProductLog.getUserId();
    int useType = scanProductLog.getUseType(); // 0 pc 1 移动 2 小程序

    String useTypeName = useType == 0 ? "PC端" : (useType == 1 ? "移动端" : "小程序端");

    // 获取Hbase中当前用户的数据
    String tableName = "userflaginfo";
    String rowKey = userId + "";
    String familyName = "userbehavior";
    String colum = "useTypeList";
    String mapData = HbaseUtil.getData(tableName, rowKey, familyName, colum);

    // Hbase中统计的是每个品牌的浏览次数
    Map<String, Long> map = new HashMap<>();
    if (StringUtils.isNotBlank(mapData)) {
      map = JSONObject.parseObject(mapData, Map.class);
    }

    // 获取之前的终端偏好
    String maxUseTypeBefore = MapUtils.getMaxInMap(map);

    // 之前这个设备的使用次数，使用过提取，没使用过为0
    Long preUseType = map.get(useTypeName) == null ? 0L : map.get(useTypeName);
    map.put(useTypeName, preUseType + 1);
    String finalString = JSONObject.toJSONString(map);
    HbaseUtil.putData(tableName, rowKey, familyName, colum, finalString);

    // 增加现在的数据后，最大的值做对比
    String maxUseTypeCurrent = MapUtils.getMaxInMap(map);
    if (StringUtils.isNotBlank(maxUseTypeCurrent) && !maxUseTypeCurrent.equals(maxUseTypeBefore)) {
      // 改变喜好前的品牌
      UseTypeInfo useTypeInfoLastTime = new UseTypeInfo();
      useTypeInfoLastTime.setUserType(maxUseTypeBefore);
      useTypeInfoLastTime.setCount(-1);
      useTypeInfoLastTime.setGroupField("==useType==" + maxUseTypeBefore);
      out.collect(useTypeInfoLastTime);
    }
    // 改变后的喜好
    UseTypeInfo useTypeInfoNow = new UseTypeInfo();
    useTypeInfoNow.setUserType(maxUseTypeCurrent);
    useTypeInfoNow.setCount(1);
    useTypeInfoNow.setGroupField("==useType==" + maxUseTypeCurrent);
    out.collect(useTypeInfoNow);
    // 我认为此处变化了的就减一，和加一，如果没变化不用写出

    // 更新用户最新偏好
    colum = "useType";
    HbaseUtil.putData(tableName, rowKey, familyName, colum, maxUseTypeCurrent);
  }
}
