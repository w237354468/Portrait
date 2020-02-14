package com.wzq.log;

import java.io.Serializable;

public class ScanProductLog  implements Serializable {

  private int productId;
  private int productTypeId; // 商品类别id
  private String scanTime; //   浏览时间
  private String stayTime; // 停留时间
  private int userId;
  private int useType;
  private String ip; //访问ip

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

  public String getScanTime() {
    return scanTime;
  }

  public void setScanTime(String scanTime) {
    this.scanTime = scanTime;
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

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }
}
