package com.wzq.reduce;

import com.wzq.entity.UseTypeInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class UseTypeReduce implements ReduceFunction<UseTypeInfo> {

  @Override
  public UseTypeInfo reduce(UseTypeInfo value1, UseTypeInfo value2) throws Exception {
    String userType = value1.getUserType();
    long coung1 = value1.getCount();
    long count2 = value2.getCount();

    UseTypeInfo resultUseTypeInfo = new UseTypeInfo();
    resultUseTypeInfo.setUserType(userType);
    resultUseTypeInfo.setCount(count2 + coung1);
    return resultUseTypeInfo;
  }
}
