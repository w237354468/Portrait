package com.wzq.map;

import com.alibaba.fastjson.JSONObject;
import com.wzq.entity.BrandLike;
import com.wzq.kafkause.KafkaEvent;
import com.wzq.log.ScanProductLog;
import com.wzq.util.HbaseUtil;
import com.wzq.utils.MapUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class BrandLikeMap implements FlatMapFunction<KafkaEvent, BrandLike> {

  @Override
  public void flatMap(KafkaEvent value, Collector<BrandLike> out) throws Exception {

    // 获取的是每条浏览数据
    String data = value.getWord();
    ScanProductLog scanProductLog = JSONObject.parseObject(data, ScanProductLog.class);

    int userId = scanProductLog.getUserId();
    String brand = scanProductLog.getBrand(); // 给品牌编号

    // 获取Hbase中当前用户的数据
    String tableName = "userflaginfo";
    String rowKey = userId + "";
    String familyName = "userbehavior";
    String colum = "brandlist";
    String mapData = HbaseUtil.getData(tableName, rowKey, familyName, colum);

    // Hbase中统计的是每个品牌的浏览次数
    Map<String, Long> map = new HashMap<>();
    if (StringUtils.isNotBlank(mapData)) {
      map = JSONObject.parseObject(mapData, Map.class);
    }

    // 获取之前的品牌偏好
    String brandLikeBefore = MapUtils.getMaxInMap(map);

    Long preBrand = map.get(brand) == null ? 0L : map.get(brand);
    map.put(brand, preBrand + 1);
    String finalString = JSONObject.toJSONString(map);
    HbaseUtil.putData(tableName, rowKey, familyName, colum, finalString);
    String maxBrandCurrent = MapUtils.getMaxInMap(map);
    if (StringUtils.isNotBlank(maxBrandCurrent) && !maxBrandCurrent.equals(brandLikeBefore)) {
      // 改变喜好前的品牌
      BrandLike brandLikeLastTime = new BrandLike();
      brandLikeLastTime.setBrand(brandLikeBefore);
      brandLikeLastTime.setCount(-1);
      brandLikeLastTime.setGroupByField("==brandlike==" + brandLikeBefore);
      out.collect(brandLikeLastTime);
    }
    // 改变后的喜好
    BrandLike brandLikeNow = new BrandLike();
    brandLikeNow.setBrand(maxBrandCurrent);
    brandLikeNow.setCount(1);
    brandLikeNow.setGroupByField("==brandlike==" + maxBrandCurrent);
    out.collect(brandLikeNow);

    // 更新用户最新偏好
    colum = "brandlike";
    HbaseUtil.putData(tableName, rowKey, familyName, colum, maxBrandCurrent);
  }
}
