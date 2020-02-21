package com.wzq.entity;

public class LocationData {
  private String phone;
  private String imei;
  private String location;
  private String timeStamp;
  private String dayOrNight;

  public LocationData() {}

  public LocationData(
      String phone, String imei, String location, String timeStamp, String dayOrNight) {
    this.phone = phone;
    this.imei = imei;
    this.location = location;
    this.timeStamp = timeStamp;
    this.setDayOrNight(dayOrNight);
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getDayOrNight() {
    return dayOrNight;
  }

  public void setDayOrNight(String dayOrNight) {
    this.dayOrNight = dayOrNight;
  }
}
