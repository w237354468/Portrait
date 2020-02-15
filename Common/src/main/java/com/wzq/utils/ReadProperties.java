package com.wzq.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

//这里会默认加载配置文件
public class ReadProperties {
  public static Config config = ConfigFactory.load("wzq.properties");

  public static String getKey(String key) {
    return config.getString(key).trim();
  }
  public static String getKey(String key,String fileName) {
    config = ConfigFactory.load(fileName);
    return config.getString(key).trim();
  }
}
