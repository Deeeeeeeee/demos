package com.sealde.thread.container;

import com.sealde.thread.tool.SleepTool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: sealde
 * @Date: 2019/10/7 下午5:52
 */
public class HashMapTestDemo {
    private static Map<String, String> map = new HashMap<>();
//    private static Map<String, String> map = new ConcurrentHashMap<>();

    // 测试多个线程同时 put 的情况
    private static void testPut() throws InterruptedException {
        // 线程1 => t1
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 999; i++) {
                    map.put("thread1_key" + i, "thread1_value" + i);
                }
            }
        });
        // 线程2 => t2
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 999; i++) {
                    map.put("thread2_key" + i, "thread2_value" + i);
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(map.size());
    }

    // 测试多个线程同时 put 的时候，get 的情况
    private static void testPutGet() {
        map.put("test get", "ok");
        System.out.println(map.get("test get"));
        SleepTool.sleep(1000);
        // 线程1 => get
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String get = map.get("test get");
                    if (get == null) {
                        System.out.println("null");
                        break;
                    }
                }
            }
        });
        // 线程2 => put
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    map.put("thread2_key" + i, "thread2_value" + i);
                }
            }
        });
        t1.start();
        t2.start();
    }

    public static void main(String[] args) throws InterruptedException {
        testPut();
//        testPutGet();
    }
}
