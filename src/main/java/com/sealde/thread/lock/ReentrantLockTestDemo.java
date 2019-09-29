package com.sealde.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: sealde
 * @Date: 2019/9/27 下午3:02
 */
public class ReentrantLockTestDemo {
    private static ReentrantLock lock = new ReentrantLock();

    private static class LockThread extends Thread {
        @Override
        public void run() {
            lock.lock();
            System.out.println("continue");
        }
    }

    private static class LockLockInterruptiblyThread extends Thread {
        @Override
        public void run() {
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 主线先拿到锁
        lock.lock();
        Thread.sleep(100);

        // 测试线程再拿锁的时候就被阻塞住了
        LockThread thread = new LockThread();
//        LockLockInterruptiblyThread thread = new LockLockInterruptiblyThread();
        thread.start();
        Thread.sleep(100);

        // 中断测试线程，LockThread 一直阻塞，而 LockLockInterruptiblyThread 中断了获取锁的过程
        thread.interrupt();
        System.out.println("finished");
    }
}
