package com.wzq.entity.location;

public class JizhanLocationTable {

  private String lac_ci_code;
  private String area;
  private String JizhanLocation;

  public JizhanLocationTable() {}

  public JizhanLocationTable(Long lac, Long ci, Long code, String area, String JizhanLocation) {
    this.setLac_ci_code(String.format("%s_%s_%s", lac, ci, code));
    this.setArea(area);
    this.setJizhanLocation(JizhanLocation);
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getJizhanLocation() {
    return JizhanLocation;
  }

  public void setJizhanLocation(String jizhanLocation) {
    JizhanLocation = jizhanLocation;
  }

  public String getLac_ci_code() {
    return lac_ci_code;
  }

  public void setLac_ci_code(String lac_ci_code) {
    this.lac_ci_code = lac_ci_code;
  }
}
