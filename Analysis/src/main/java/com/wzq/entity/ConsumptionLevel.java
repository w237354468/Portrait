package com.wzq.entity;

public class ConsumptionLevel {
  private String userId;
  private Long count;
  private String groupField;
  private String consumption; // high level ,medium level,low level
  private String amounttotal;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public String getGroupField() {
    return groupField;
  }

  public void setGroupField(String groupField) {
    this.groupField = groupField;
  }

  public String getConsumption() {
    return consumption;
  }

  public void setConsumption(String consumption) {
    this.consumption = consumption;
  }

  public String getAmounttotal() {
    return amounttotal;
  }

  public void setAmounttotal(String amounttotal) {
    this.amounttotal = amounttotal;
  }
}
