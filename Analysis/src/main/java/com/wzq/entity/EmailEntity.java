package com.wzq.entity;

public class EmailEntity {

  private String  emailType; // 邮箱类型
  private long count;
  private String  gourpField;

  public String  getEmailType() {
    return emailType;
  }

  public void setEmailType(String  emailType) {
    this.emailType = emailType;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public String getGourpField() {
    return gourpField;
  }

  public void setGourpField(String gourpField) {
    this.gourpField = gourpField;
  }
}
