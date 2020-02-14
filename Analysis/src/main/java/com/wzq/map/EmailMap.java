package com.wzq.map;

import com.wzq.entity.EmailEntity;
import com.wzq.util.EmailUtil;
import com.wzq.util.HbaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class EmailMap implements MapFunction<String, EmailEntity> {

  public EmailEntity map(String s) throws Exception {
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

    String emailType = EmailUtil.getEmailType(email);
    String rowKey = userId;
    String tableName = "userFlagInfo";
    String familyName = "baseInfo";
    String column = "carrierInfo"; // 运营商
    HbaseUtil.putData(tableName, rowKey, familyName, column, emailType);

    String groupField = "yearbase==" + emailType;
    EmailEntity emailEntity = new EmailEntity();
    emailEntity.setEmailType(emailType);
    emailEntity.setCount(1);
    emailEntity.setGourpField(groupField);
    return emailEntity;
  }
}
