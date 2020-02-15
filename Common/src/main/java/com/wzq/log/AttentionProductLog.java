package com.wzq.log;

import java.io.Serializable;

public class AttentionProductLog implements Serializable {

  private int productId; //商品id
  private int productTypeId; // 商品类别id
  private String operatorTime; //  操作时间
  private int operatorType; // 0 关注 1 取消
  private String stayTime; //停留时间
  private int userId;
  private int useType;
  private String ip; //访问ip
  private String brand; //品牌

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getProductTypeId() {
    return productTypeId;
  }

  public void setProductTypeId(int productTypeId) {
    this.productTypeId = productTypeId;
  }

  public String getOperatorTime() {
    return operatorTime;
  }

  public void setOperatorTime(String operatorTime) {
    this.operatorTime = operatorTime;
  }

  public int getOperatorType() {
    return operatorType;
  }

  public void setOperatorType(int operatorType) {
    this.operatorType = operatorType;
  }

  public String getStayTime() {
    return stayTime;
  }

  public void setStayTime(String stayTime) {
    this.stayTime = stayTime;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getUseType() {
    return useType;
  }

  public void setUseType(int useType) {
    this.useType = useType;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }
}
