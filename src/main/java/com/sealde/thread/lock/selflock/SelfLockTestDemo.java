package com.sealde.thread.lock.selflock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: sealde
 * @Date: 2019/9/27 下午7:37
 */
public class SelfLockTestDemo {

    private static class LockThread extends Thread {
        private Lock lock;

        public LockThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            System.out.println("continue");
        }
    }

    private static class LockLockInterruptiblyThread extends Thread {
        private Lock lock;

        public LockLockInterruptiblyThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testLockAndLockInterruptibly(Lock lock) throws InterruptedException {
        // 主线先拿到锁
        lock.lock();
        Thread.sleep(100);

        // 测试线程再拿锁的时候就被阻塞住了
        LockThread thread = new LockThread(lock);
//        LockLockInterruptiblyThread thread = new LockLockInterruptiblyThread(lock);
        thread.start();
        Thread.sleep(100);

        // 中断测试线程，LockThread 一直阻塞，而 LockLockInterruptiblyThread 中断了获取锁的过程
        thread.interrupt();
        System.out.println("finished");
    }

    public static void main(String[] args) throws InterruptedException {
//        Lock lock = new ReentrantLock();
        Lock lock = new SelfLock();
        testLockAndLockInterruptibly(lock);
    }
}
