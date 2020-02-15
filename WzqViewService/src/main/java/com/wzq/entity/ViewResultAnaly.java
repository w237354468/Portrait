package com.wzq.entity;

import java.util.List;

public class ViewResultAnaly {

  private List<String> infoList; // 分组list x轴
  private List<Long> count; // 数量
  private String result;
  private String typeNma; // 标签类型名称
  private String labelValue; // 标签值

  private List<ViewResultAnaly> list;

  public List<ViewResultAnaly> getList() {
    return list;
  }

  public void setList(List<ViewResultAnaly> list) {
    this.list = list;
  }

  public ViewResultAnaly() {}

  public ViewResultAnaly(String typeNma, String labelValue) {
    this.typeNma = typeNma;
    this.labelValue = labelValue;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getTypeNma() {
    return typeNma;
  }

  public void setTypeNma(String typeNma) {
    this.typeNma = typeNma;
  }

  public String getLabelValue() {
    return labelValue;
  }

  public void setLabelValue(String labelValue) {
    this.labelValue = labelValue;
  }

  public List<String> getInfoList() {
    return infoList;
  }

  public void setInfoList(List<String> infoList) {
    this.infoList = infoList;
  }

  public List<Long> getCount() {
    return count;
  }

  public void setCount(List<Long> count) {
    this.count = count;
  }
}
