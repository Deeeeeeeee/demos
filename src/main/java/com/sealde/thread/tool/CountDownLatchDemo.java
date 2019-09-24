package com.sealde.thread.tool;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 等待初始化线程完成例子
 * @Author: sealde
 * @Date: 2019/9/24 下午3:50
 */
public class CountDownLatchDemo {
    private static final CountDownLatch startSignal = new CountDownLatch(1);
    private static final CountDownLatch latch = new CountDownLatch(5);

    private static class MyTread extends Thread {
        @Override
        public void run() {
            try {
                startSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("init thread " + this.getName() + " do init work");
            SleepTool.sleep(500);
            latch.countDown();
            System.out.println("init thread" + this.getName() + " continue work");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            MyTread myTread = new MyTread();
            myTread.start();
        }
        System.out.println("main thread let init thread work");
        // 开始信号
        startSignal.countDown();
        System.out.println("main thread wait init work finished");
        // 等待初始化完成
        latch.await();

        System.out.println("main thread continue work");
    }
}
