package com.wzq;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MarkUtil {

  public static String getSexMark(String age) {
    return age.equals("男") ? "1" : "0";
  }

  public static String getYearBaseByAge(String age) {
    Calendar calendar = Calendar.getInstance();
    // now year
    calendar.setTime(new Date());
    // 加上age年
    calendar.add(Calendar.YEAR, -Integer.parseInt(age));
    Date newdata = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    String newdataString = dateFormat.format(newdata);
    int newDataInt = Integer.parseInt(newdataString);

    if (newDataInt > 1940 && newDataInt < 1950) {
      return "50年代";
    }
    return "未知";
  }

  // 这里可以参考 ZLN写的规则
  public static String getEmailType(String email) {

    if (email.contains("163.com") || email.contains("126.com")) {
      return "网易邮箱用户";
    } else if (email.contains("qq.com")) {
      return "腾讯邮箱";
    }
    return "其他邮箱用户";
  }

  public static int getDaysBetweenByStartAndEnd(
      String startTime, String endTime, String dateFormatString) throws ParseException {

    DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
    Date start = dateFormat.parse(startTime);
    Date end = dateFormat.parse(endTime);
    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();
    startCalendar.setTime(start);
    endCalendar.setTime(end);
    int days = 0;
    while (startCalendar.before(endCalendar)) {
      startCalendar.add(Calendar.DAY_OF_YEAR, 1);
      days += 1;
    }
    return days;
  }
}
