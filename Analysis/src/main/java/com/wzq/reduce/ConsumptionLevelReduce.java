package com.wzq.reduce;

import com.wzq.entity.ConsumptionLevel;
import com.wzq.util.HbaseUtil;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.util.Collector;

public class ConsumptionLevelReduce
    implements GroupReduceFunction<ConsumptionLevel, ConsumptionLevel> {

  @Override
  public void reduce(Iterable<ConsumptionLevel> values, Collector<ConsumptionLevel> out)
      throws Exception {

    String userId = "";
    Iterator<ConsumptionLevel> iterator = values.iterator();
    int sum = 0;
    double totalAmount = 0.0;
    while (iterator.hasNext()) {
      ConsumptionLevel next = iterator.next();
      userId = next.getUserId();
      String amountTotal = next.getAmounttotal();
      double amountToalDouble = Double.parseDouble(amountTotal);
      totalAmount += amountToalDouble;
      sum++;
    }
    double avr = totalAmount / sum;
    String flag = "low";
    if (avr > 1000 && avr < 5000) {
      flag = "medium";
    } else if (avr >= 5000) {
      flag = "high";
    }
    String tableName = "useflaginfo";
    String rowKey = userId + "";
    String family = "consumerinfo";
    String colum = "consumptionLevel";
    String reserveType = HbaseUtil.getData(tableName, rowKey, family, colum);
    if (StringUtils.isBlank(reserveType)) {
      ConsumptionLevel consumptionLevel = new ConsumptionLevel();
      consumptionLevel.setUserId(userId);
      consumptionLevel.setCount(1L);
      consumptionLevel.setGroupField("consumptionLevelfinal=" + flag);
      out.collect(consumptionLevel);
      // unequal
    } else if (!reserveType.equals(flag)) {
      ConsumptionLevel consumptionLevel1 = new ConsumptionLevel();
      consumptionLevel1.setConsumption(flag);
      consumptionLevel1.setCount(1L);
      consumptionLevel1.setGroupField("consumptionLevelfinal+" + flag);
      ConsumptionLevel consumptionLevel2 = new ConsumptionLevel();
      consumptionLevel2.setConsumption(reserveType);
      consumptionLevel2.setCount(-1L);
      consumptionLevel2.setGroupField("consumptionLevelfinal" + reserveType);
      out.collect(consumptionLevel1);
      out.collect(consumptionLevel2);
    }
    HbaseUtil.putData(tableName, rowKey, family, colum, flag);
  }
}
