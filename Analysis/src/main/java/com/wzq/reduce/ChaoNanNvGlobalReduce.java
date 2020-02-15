package com.wzq.reduce;

import com.wzq.entity.ChaoNanNvInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class ChaoNanNvGlobalReduce implements ReduceFunction<ChaoNanNvInfo> {

  @Override
  public ChaoNanNvInfo reduce(ChaoNanNvInfo value1, ChaoNanNvInfo value2) throws Exception {
    String chaoType = value1.getChaoType();

    long count1 = value1.getCount();
    long count2 = value2.getCount();
    ChaoNanNvInfo resultChaoNanNvInfo = new ChaoNanNvInfo();
    resultChaoNanNvInfo.setChaoType(chaoType);
    resultChaoNanNvInfo.setCount((int) (count1 + count2));


    return null;
  }
}
