package com.wzq.map;

import com.alibaba.fastjson.JSONObject;
import com.wzq.entity.ChaoNanNvInfo;
import com.wzq.kafkause.KafkaEvent;
import com.wzq.log.ScanProductLog;
import com.wzq.utils.ReadProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class ChaoNanNvMap implements FlatMapFunction<KafkaEvent, ChaoNanNvInfo> {

  @Override
  public void flatMap(KafkaEvent value, Collector<ChaoNanNvInfo> out) throws Exception {
    String word = value.getWord();
    ScanProductLog scanProductLog = JSONObject.parseObject(word, ScanProductLog.class);
    int userId = scanProductLog.getUserId();
    int productId = scanProductLog.getProductId();
    ChaoNanNvInfo chaoNanNvInfo = new ChaoNanNvInfo();
    chaoNanNvInfo.setUserId("userid" + userId);

    // 有信息才写出 根据一个商品的信息来判断
    String chaoliuIdForUser = ReadProperties.getKey(productId + "", "productchaoliu.properties");
    if (StringUtils.isBlank(chaoliuIdForUser)) {
      chaoNanNvInfo.setChaoType(chaoliuIdForUser);
      chaoNanNvInfo.setProductId("productId" + productId);
      chaoNanNvInfo.setGroupField("chaonannv==" + userId);
      chaoNanNvInfo.setCount(1);
      out.collect(chaoNanNvInfo);
    }
  }
}
