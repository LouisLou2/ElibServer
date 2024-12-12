package com.leo.elib.config;

public class ServiceNetConfig {
  static private final String ResourceServerAddress = "http://192.168.163.252:9022/";
  public static String equip(String path){
    return ResourceServerAddress.concat(path);
  }
}