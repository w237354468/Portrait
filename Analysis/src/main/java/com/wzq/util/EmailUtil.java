package com.wzq.util;

public class EmailUtil {

  //这里可以参考 ZLN写的规则
  public static String getEmailType(String email){

    if(email.contains("163.com")||email.contains("126.com")){
      return "网易邮箱用户";
    }
    return "其他邮箱用户";

  }
}
