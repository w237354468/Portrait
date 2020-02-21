package com.wzq.entity.basic;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MarkObject implements Serializable {

  private String phone;
  private String sexMark;
  private String emailMark;
  private String ageMark;
  private String graduationMark;

  public MarkObject() {}

  public MarkObject(
      String phone, String sexMark, String emailMark, String ageMark, String graduationMark) {
    this.phone = phone;
    this.sexMark = sexMark;
    this.emailMark = emailMark;
    this.ageMark = ageMark;
    this.graduationMark = graduationMark;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getSexMark() {
    return sexMark;
  }

  public void setSexMark(String sexMark) {
    this.sexMark = sexMark;
  }

  public String getEmailMark() {
    return emailMark;
  }

  public void setEmailMark(String emailMark) {
    this.emailMark = emailMark;
  }

  public String getAgeMark() {
    return ageMark;
  }

  public void setAgeMark(String ageMark) {
    this.ageMark = ageMark;
  }

  public String getGraduationMark() {
    return graduationMark;
  }

  public void setGraduationMark(String graduationMark) {
    this.graduationMark = graduationMark;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MarkObject that = (MarkObject) o;

    return new EqualsBuilder()
        .append(phone, that.phone)
        .append(sexMark, that.sexMark)
        .append(emailMark, that.emailMark)
        .append(ageMark, that.ageMark)
        .append(graduationMark, that.graduationMark)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(phone)
        .append(sexMark)
        .append(emailMark)
        .append(ageMark)
        .append(graduationMark)
        .toHashCode();
  }

  @Override
  public String toString() {
    return "MarkObject{" +
        "phone='" + phone + '\'' +
        ", sexMark='" + sexMark + '\'' +
        ", emailMark='" + emailMark + '\'' +
        ", ageMark='" + ageMark + '\'' +
        ", graduationMark='" + graduationMark + '\'' +
        '}';
  }
}
