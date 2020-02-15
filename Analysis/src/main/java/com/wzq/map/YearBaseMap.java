package com.wzq.map;

import com.wzq.entity.YearBase;
import com.wzq.util.DateUtil;
import com.wzq.util.HbaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class YearBaseMap implements MapFunction<String, YearBase> {

  public YearBase map(String s) throws Exception {
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

    String  yearBaseType = DateUtil.getYearBaseByAge(age);
    String tableName = "userFlagInfo";
    String rowKey = userId;
    String familyName = "baseInfo";
    String column = "yearbase"; // 年代
    HbaseUtil.putData(tableName, rowKey, familyName, column, yearBaseType);

    String groupField = "yearbase==" + yearBaseType;
    YearBase yearBase = new YearBase();
    yearBase.setYearType(yearBaseType);
    yearBase.setCount(1);
    yearBase.setGroupField(groupField);
    return yearBase;
  }
}
