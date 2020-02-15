package com.wzq.reduce;

import com.wzq.entity.EmailEntity;
import org.apache.flink.api.common.functions.ReduceFunction;

public class EmailReduce implements ReduceFunction<EmailEntity> {

  public EmailEntity reduce(EmailEntity emailEntity, EmailEntity t1) throws Exception {
    Integer type = emailEntity.getEmailType();

    long count1 = emailEntity.getCount();
    long count2 = t1.getCount();

    EmailEntity resultEmailEntity = new EmailEntity();
    resultEmailEntity.setGourpField(type);
    resultEmailEntity.setCount(count1 + count2);
    return resultEmailEntity;
  }
}
