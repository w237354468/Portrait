package com.wzq.map;

import com.wzq.entity.ConsumptionLevel;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class ConsumptionMap implements MapFunction<String, ConsumptionLevel> {

  @Override
  public ConsumptionLevel map(String value) throws Exception {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    String[] orderInfos = value.split(",");
    String id = orderInfos[0];
    String productId = orderInfos[1];
    String productTypeId = orderInfos[2];
    String createtime = orderInfos[3];
    String payAmount = orderInfos[4];
    String payType = orderInfos[5];
    String payTime = orderInfos[6];
    String payStatus = orderInfos[7];
    String couponAmount = orderInfos[8];
    String totalAmount = orderInfos[9];
    String refundAmount = orderInfos[10];
    String num = orderInfos[11];
    String userId = orderInfos[12];
    ConsumptionLevel consumptionLevel = new ConsumptionLevel();
    consumptionLevel.setUserId(userId);
    consumptionLevel.setAmounttotal(totalAmount);
    consumptionLevel.setGroupField("==consumerLevel" + userId);
    return consumptionLevel;
  }
}
