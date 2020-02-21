package com.wzq.entity.basic;

import java.io.Serializable;

public class ProfileEvent implements Serializable {

  private String phone;
  private String sex;
  private String email;
  private String age;
  private String graduation;

  public ProfileEvent(String phone, String sex, String email, String age, String graduation) {
    this.phone = phone;
    this.sex = sex;
    this.email = email;
    this.age = age;
    this.graduation = graduation;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getGraduation() {
    return graduation;
  }

  public void setGraduation(String graduation) {
    this.graduation = graduation;
  }
}
