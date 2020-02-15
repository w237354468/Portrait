package com.wzq.reduce;

import com.wzq.entity.ConsumptionLevel;
import org.apache.flink.api.common.functions.ReduceFunction;

public class ConsumptionResultReduce implements ReduceFunction<ConsumptionLevel> {

  @Override
  public ConsumptionLevel reduce(ConsumptionLevel value1, ConsumptionLevel value2)
      throws Exception {
    String type = value1.getConsumption();
    Long count1 = value1.getCount();
    Long count2 = value2.getCount();
    ConsumptionLevel result = new ConsumptionLevel();
    result.setConsumption(type);
    result.setCount(count1 + count2);
    return result;
  }
}
