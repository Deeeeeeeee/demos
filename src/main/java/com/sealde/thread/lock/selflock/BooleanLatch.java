package com.sealde.thread.lock.selflock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Latch 是共享式的
 * state 只有 0 和 1 的状态
 * @Author: sealde
 * @Date: 2019/9/27 下午9:10
 */
public class BooleanLatch {
    private static class Sync extends AbstractQueuedSynchronizer {
        boolean isSignalled() {
            return getState() != 0;
        }

        @Override
        protected int tryAcquireShared(int ignore) {
            return isSignalled() ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int ignore) {
            setState(1);
            return true;
        }
    }

    private final Sync sync = new Sync();

    public boolean isSignalled() {
        return sync.isSignalled();
    }

    // 调用 signal 时，将状态改为 1，等待线程将会获得锁，然后继续运行，而且状态会传播下去，直到所有阻塞的线程都释放完毕
    public void signal() {
        sync.releaseShared(1);
    }

    // 调用 await 时，state == 0 的话，tryAcquireShared 返回 -1，线程将进入同步队列当中，开始自旋等待 signal
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }
}
