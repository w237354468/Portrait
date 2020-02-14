package com.wzq.reduce;

import com.wzq.entity.BaijiaInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.common.functions.ReduceFunction;

public class BaijiaReduce implements ReduceFunction<BaijiaInfo> {

  @Override
  public BaijiaInfo reduce(BaijiaInfo baijiaInfo, BaijiaInfo t1) throws Exception {
    String userId = baijiaInfo.getUserId();
    List<BaijiaInfo> baijiaInfoList1 = baijiaInfo.getList();
    List<BaijiaInfo> baijiaInfoList2 = t1.getList();
    ArrayList<BaijiaInfo> finalList = new ArrayList<>();
    finalList.addAll(baijiaInfoList1);
    finalList.addAll(baijiaInfoList2);

    BaijiaInfo resultBaijiaInfo = new BaijiaInfo();
    resultBaijiaInfo.setUserId(userId);
    resultBaijiaInfo.setList(finalList);

    return resultBaijiaInfo;
  }
}
