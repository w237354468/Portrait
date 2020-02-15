package com.wzq.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  public static String  getYearBaseByAge(String age) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.YEAR, Integer.parseInt(age));
    Date newdata = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    String newdataString = dateFormat.format(newdata);
    int newDataInt = Integer.parseInt(newdataString);
    if (newDataInt > 1940 && newDataInt < 1950) {
      return "50年代";
    }
    return "未知";
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
