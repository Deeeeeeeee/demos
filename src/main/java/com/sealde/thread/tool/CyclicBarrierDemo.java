package com.sealde.thread.tool;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * 计算线程都达到栅栏时，栅栏才开放并继续执行。Gather线程也开始工作
 * @Author: sealde
 * @Date: 2019/9/24 下午4:04
 */
public class CyclicBarrierDemo {
    private static class CalThread extends Thread {
        private CyclicBarrier barrier;
        private boolean isSleep;        // 随机睡眠
        private ConcurrentHashMap<CalThread, Integer> map;

        public CalThread(CyclicBarrier barrier, boolean isSleep, ConcurrentHashMap<CalThread, Integer> map) {
            this.barrier = barrier;
            this.isSleep = isSleep;
            this.map = map;
        }

        @Override
        public void run() {
            Random r = new Random();
            int i = r.nextInt(10000);
            map.put(this, i);
            System.out.println("thread cal result " + i);
            if (isSleep) {
                SleepTool.sleep(2000);
            }
            try {
                System.out.println("thread " + this.getName() + " wait signal");
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 栅栏开放时，开始工作
            System.out.println("thread " + this.getName() + " continue ...");
        }
    }

    private static class GatherThread extends Thread {
        private ConcurrentHashMap<CalThread, Integer> map;

        public GatherThread(ConcurrentHashMap<CalThread, Integer> map) {
            this.map = map;
        }

        @Override
        public void run() {
            System.out.println(map.values());
        }
    }

    public static void main(String[] args) {
        ConcurrentHashMap<CalThread, Integer> map = new ConcurrentHashMap<>();
        // 当所有计算线程到达栅栏时，GatherThread 开始工作
        CyclicBarrier barrier = new CyclicBarrier(5, new GatherThread(map));
        Random boolRandom = new Random();
        for (int i = 0; i < 5; i++) {
            CalThread calThread = new CalThread(barrier, boolRandom.nextBoolean(), map);
            calThread.start();
        }
    }
}
