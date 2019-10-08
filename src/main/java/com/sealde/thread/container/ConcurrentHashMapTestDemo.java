package com.sealde.thread.container;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: sealde
 * @Date: 2019/10/8 下午12:17
 */
public class ConcurrentHashMapTestDemo {
    // 测试扩容
    private static void testResize() {
        // jdk8 请在 ConcurrentHashMap 2277 行打下断点
        // 默认容量为 16，当达到 12 个元素的时候，会进行扩容
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 16; i++) {
            map.put("a" + i, "b");
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        testResize();
    }
}
