package com.wzq.entity;

import java.util.List;

// 统计同一单中平均值和最大值的差
public class BaijiaInfo {

  private String baijiascore; // 区段0-20,20-40 ... 80-100 败家指数
  private String userId;
  private String createTime;
  private String payAmount;
  private String payType;
  private String payTime;
  private String payStatus; // 0 未支付 1 已支付 2 已退款
  private String couponAmount; // 优惠卷金额
  private String totalAmount;
  private String refundAmount;
  private Long count; // 此Baijia数量
  private String groupField; // 分组

  private List<BaijiaInfo> list;

  public String getBaijiascore() {
    return baijiascore;
  }

  public void setBaijiascore(String baijiascore) {
    this.baijiascore = baijiascore;
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

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(String amount) {
    this.payAmount = amount;
  }

  public String getPayType() {
    return payType;
  }

  public void setPayType(String payType) {
    this.payType = payType;
  }

  public String getPayTime() {
    return payTime;
  }

  public void setPayTime(String payTime) {
    this.payTime = payTime;
  }

  public String getPayStatus() {
    return payStatus;
  }

  public void setPayStatus(String payStatus) {
    this.payStatus = payStatus;
  }

  public String getCouponAmount() {
    return couponAmount;
  }

  public void setCouponAmount(String couponAmount) {
    this.couponAmount = couponAmount;
  }

  public String getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(String totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(String refundAmount) {
    this.refundAmount = refundAmount;
  }

  public List<BaijiaInfo> getList() {
    return list;
  }

  public void setList(List<BaijiaInfo> list) {
    this.list = list;
  }
}
