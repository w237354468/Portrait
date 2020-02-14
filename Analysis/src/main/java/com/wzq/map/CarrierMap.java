package com.wzq.map;

import com.wzq.entity.CarrierInfo;
import com.wzq.util.CarrierUtil;
import com.wzq.util.HbaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class CarrierMap implements MapFunction<String, CarrierInfo> {

  public CarrierInfo map(String s) throws Exception {
    if (StringUtils.isBlank(s)) {
      return null;
    }
    String[] userInfos = s.split(",");
    String userId = userInfos[0];
    String username = userInfos[1];
    String sex = userInfos[2];
    String telphone = userInfos[3];
    String email = userInfos[4];
    String age = userInfos[5];
    String registerTime = userInfos[6];
    String userType = userInfos[7];

    int carrier = CarrierUtil.getCarrier(telphone);
    String carrierType = carrier == 0 ? "未知" : (carrier == 1 ? "联通" : "移动");
    String rowKey = userId;
    String tableName = "userFlagInfo";
    String familyName = "baseInfo";
    String column = "carrierInfo"; // 运营商
    HbaseUtil.putData(tableName, rowKey, familyName, column, carrierType);

    String groupField = "yearbase==" + carrierType;
    CarrierInfo carrierInfo = new CarrierInfo();
    carrierInfo.setCarrier(carrierType);
    carrierInfo.setCount(1);
    carrierInfo.setGourpField(groupField);
    return carrierInfo;
  }
}
