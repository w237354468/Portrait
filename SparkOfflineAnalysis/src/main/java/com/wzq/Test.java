package com.wzq;

import com.alibaba.fastjson.JSON;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class Test {

  public static void main(String[] args) {
    //    Calendar calendar = Calendar.getInstance();
    //    //now year
    //    calendar.setTime(new Date());
    //    calendar.add(Calendar.YEAR, -Integer.parseInt("25"));
    //    Date newdata = calendar.getTime();
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    //    String newdataString = dateFormat.format(newdata);
    //    int newDataInt = Integer.parseInt(newdataString);
    //    System.out.println(newDataInt);

    HashMap<String, Long> locationMap = new HashMap<>();
    locationMap.put("a", 1L);
    locationMap.put("b", 1543L);
    locationMap.put("c", 1234L);
    locationMap.put("d", 1134L);
    locationMap.put("e", 132L);
    ArrayList<String> topSixMap = new ArrayList<>();
    locationMap.entrySet().stream()
        .sorted(Entry.<String, Long>comparingByValue().reversed()) // reversed不生效
        .limit(6)
        .map(Entry::getKey)
        .forEach(topSixMap::add);

    topSixMap.forEach(System.out::println);
    String s = JSON.toJSONString(topSixMap);
    System.out.println(s);

    ArrayList<String > arrayList = JSON.parseObject(s, ArrayList.class);
    arrayList.forEach(System.out::println);

  }
}
