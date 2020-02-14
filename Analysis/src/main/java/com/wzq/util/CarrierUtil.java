package com.wzq.util;

import java.util.regex.Pattern;

public class CarrierUtil {

  // 进行运营商匹配
  public static final String UNICOM_PATTERN = "313";

  // 判断运营商
  public static int getCarrier(String tel) {

    boolean b1 = tel == null || tel.trim().equals("") ? false : match(UNICOM_PATTERN, tel);
    if (b1) {
      return 1; // 代表联通
    }
    // 2 3 代表不同运营商，就不写了
    return 0;
  }

  private static boolean match(String unicomPattern, String tel) {
    return Pattern.matches(unicomPattern, tel);
  }
}
