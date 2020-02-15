package com.wzq.entity;

public class BrandLike {

  private String  brand;
  private long count;
  private String groupByField;

  public String  getBrand() {
    return brand;
  }

  public void setBrand(String  brand) {
    this.brand = brand;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public String getGroupByField() {
    return groupByField;
  }

  public void setGroupByField(String groupByField) {
    this.groupByField = groupByField;
  }
}
