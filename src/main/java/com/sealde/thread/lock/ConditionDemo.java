package com.sealde.thread.lock;

import com.sealde.thread.lock.selflock.SelfLock;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: sealde
 * @Date: 2019/9/20 下午4:50
 */
public class ConditionDemo {
    public static class ThreadClazz extends Thread {
        private WaitClazz waitClazz;

        public ThreadClazz(WaitClazz clazz) {
            this.waitClazz = clazz;
        }

        // 线程进入等待状态，等待被通知
        @Override
        public void run() {
            try {
                boolean isStartSignal = new Random().nextBoolean();
                System.out.println(String.format("thread " + this.getName() + " 等待【%s】信号",
                        isStartSignal ? "开始" : "结束"));
                this.waitClazz.waitSomething(isStartSignal);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread is:" + this.getName() + ". 结束线程");
        }
    }

    public static class WaitClazz {
        // 是否需要等待的条件
        private boolean waitFlag = true;
        private ReentrantLock lock = new ReentrantLock();
//        private Lock lock = new SelfLock();
        private Condition startCd = lock.newCondition();
        private Condition endCd = lock.newCondition();

        public void waitSomething(boolean isStartSignal) throws InterruptedException {
            lock.lock();
            try {
                Condition condition = isStartSignal ? startCd : endCd;
                while (waitFlag) {
                    condition.await();
                }
                System.out.println(String.format("接收到【%s】通知，继续干活", isStartSignal ? "开始" : "结束"));
            } finally {
                lock.unlock();
            }
        }

        public void notifySomething(boolean isStartSignal) {
            lock.lock();
            try {
                waitFlag = false;
                Condition condition = isStartSignal ? startCd : endCd;
                condition.signal();
                System.out.println(String.format("发出【%s】通知信号", isStartSignal ? "开始" : "结束"));
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitClazz waitClazz = new WaitClazz();

        // 启动4个线程进行等待通知
        Thread t1 = new Thread(new ThreadClazz(waitClazz));
        Thread t2 = new Thread(new ThreadClazz(waitClazz));
        Thread t3 = new Thread(new ThreadClazz(waitClazz));
        Thread t4 = new Thread(new ThreadClazz(waitClazz));
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Thread.sleep(1000);

        // 唤醒等待中的线程
        Random r = new Random();
        waitClazz.notifySomething(r.nextBoolean());
    }
}
