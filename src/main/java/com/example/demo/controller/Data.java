package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Data {

  public static Map<String, String> mysql = new HashMap<>();
  public static Map<String, String> redis = new HashMap<>();


  static {
    mysql.put("key1", "test1");
    mysql.put("key2", "test2");
    mysql.put("key3", "test3");
    mysql.put("key4", "test4");
  }

  //3.添加定时任务
  @Scheduled(cron = "0/5 * * * * ?")
  //或直接指定时间间隔，例如：5秒
  private void configureTasks() {
    System.out.println("缓存清空。。。。");
    redis.clear();
  }


}
