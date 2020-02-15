package com.wzq.entity;

// 我可以统计日更新
public class YearBase {

  private String  yearType; // 年代类型
  private long count; // 数量
  private  String groupField;

  public String  getYearType() {
    return yearType;
  }

  public void setYearType(String  yearType) {
    this.yearType = yearType;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public String  getGroupField() {
    return groupField;
  }

  public void setGroupField(String  groupField) {
    this.groupField = groupField;
  }
}
