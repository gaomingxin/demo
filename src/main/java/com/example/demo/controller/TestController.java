package com.example.demo.controller;

import java.util.concurrent.CountDownLatch;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hc")
public class TestController {

    private CountDownLatch count = new CountDownLatch(1000);

    private class Mytask implements Runnable {

        private String key;

        public Mytask(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            count.countDown();
            test(key, 1);
        }
    }

    @RequestMapping("/ct1")
    public String test(String key, int i) {
        // 查询redis
        String s = Data.redis.get(key);
        if (null != s) {
            if (i == 0) {
                System.out.println("" + i);
            }
            System.out.println("查询缓存--------------");
        }
        if (null == s) {
            System.out.println("查询数据库--------------");
            s = Data.mysql.get(key);
            System.out.println("s=" + s);
            if (s == null) {

                System.out.println("数据库没有-------------------发生了穿透");
                return "";
            }
            Data.redis.put(key, s);
        }
        return s;
    }

    // 模仿缓存击穿
    @RequestMapping("/ct")
    public String ct(String key) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Mytask(key)).start();
        }
        count.await();
        return "ct";
    }

    // 模仿缓存雪崩
    @RequestMapping("/xb")
    public String xb(String key) throws InterruptedException {
        Data.redis.clear();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Mytask(key)).start();
            count.await();
        }
        return "ct";
    }

}
