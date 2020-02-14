package com.wzq.entity;

public class CarrierInfo {

  private String carrier; // 运营商
  private long count;
  private String gourpField; // 分组

  public String getCarrier() {
    return carrier;
  }

  public void setCarrier(String carrier) {
    this.carrier = carrier;
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
