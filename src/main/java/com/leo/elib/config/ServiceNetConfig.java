package com.leo.elib.config;

public class ServiceNetConfig {
  static private final String ResourceServerAddress = "http://10.0.2.2:9022/";
  public static String equip(String path){
    return ResourceServerAddress.concat(path);
  }
}