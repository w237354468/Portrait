package com.wzq.entity;

public class ChaoNanNvInfo {

  // 此处我可以利用使用APP信息
  private String ChaoType; // 0 潮男 1 潮女
  private String userId;
  private String productId;
  private int count;
  private String groupField;

  public String getChaoType() {
    return ChaoType;
  }

  public void setChaoType(String chaoType) {
    ChaoType = chaoType;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getGroupField() {
    return groupField;
  }

  public void setGroupField(String groupField) {
    this.groupField = groupField;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }
}
