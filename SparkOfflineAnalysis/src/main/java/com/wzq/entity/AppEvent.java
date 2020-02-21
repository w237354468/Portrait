package com.wzq.entity;

public class AppEvent {
  private String phone;
  private String imei;
  private String system;
  private String appId;
  private String timestamp;

  public AppEvent() {}

  public AppEvent(
      String phone, String imei, String system, String appId, String timestamp) {
    this.phone = phone;
    this.imei = imei;
    this.system = system;
    this.appId = appId;
    this.timestamp = timestamp;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getSystem() {
    return system;
  }

  public void setSystem(String system) {
    this.system = system;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }
}
