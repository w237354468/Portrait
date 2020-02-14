package com.wzq.reduce;

import com.wzq.entity.YearBase;
import org.apache.flink.api.common.functions.ReduceFunction;

public class YearBaseReduce implements ReduceFunction<YearBase> {

  public YearBase reduce(YearBase yearBase, YearBase t1) throws Exception {
    String type = yearBase.getYearType();
    long count1 = yearBase.getCount();

    long count2 = t1.getCount();

    YearBase resultYearBase = new YearBase();
    resultYearBase.setGroupField(type);
    resultYearBase.setCount(count1 + count2);
    return resultYearBase;
  }
}
