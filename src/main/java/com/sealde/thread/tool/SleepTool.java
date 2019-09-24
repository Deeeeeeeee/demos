package com.sealde.thread.tool;

/**
 * @Author: sealde
 * @Date: 2019/9/24 下午12:06
 */
public class SleepTool {
    public static void sleep(int mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
