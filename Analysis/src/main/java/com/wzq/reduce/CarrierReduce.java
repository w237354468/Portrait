package com.wzq.reduce;

import com.wzq.entity.CarrierInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class CarrierReduce implements ReduceFunction<CarrierInfo> {

  public CarrierInfo reduce(CarrierInfo carrierInfo, CarrierInfo t1) throws Exception {
    String type = carrierInfo.getCarrier();

    long count1 = carrierInfo.getCount();
    long count2 = t1.getCount();

    CarrierInfo resultCarrierInfo = new CarrierInfo();
    resultCarrierInfo.setGourpField(type);
    resultCarrierInfo.setCount(count1 + count2);
    return resultCarrierInfo;
  }
}
