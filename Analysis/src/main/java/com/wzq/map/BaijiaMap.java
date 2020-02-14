package com.wzq.map;

import com.wzq.entity.BaijiaInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class BaijiaMap implements MapFunction<String, BaijiaInfo> {

  public BaijiaInfo map(String s) throws Exception {
    if (StringUtils.isBlank(s)) {
      return null;
    }
    String[] orderInfos = s.split(",");
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

    BaijiaInfo baijiaInfo = new BaijiaInfo();
    baijiaInfo.setUserId(userId);
    baijiaInfo.setCreateTime(createtime);
    baijiaInfo.setPayAmount(payAmount);
    baijiaInfo.setPayType(payType);
    baijiaInfo.setPayTime(payTime);
    baijiaInfo.setPayStatus(payStatus);
    baijiaInfo.setCouponAmount(couponAmount);
    baijiaInfo.setTotalAmount(totalAmount);
    baijiaInfo.setRefundAmount(refundAmount);
    String groupField = "baijia==" + userId;
    baijiaInfo.setGroupField(groupField);

    List<BaijiaInfo> list = new ArrayList<>();
    list.add(baijiaInfo); //list里添加自己
    baijiaInfo.setList(list);
    return baijiaInfo;
  }
}
