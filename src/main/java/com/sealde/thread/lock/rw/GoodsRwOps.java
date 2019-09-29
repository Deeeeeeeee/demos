package com.sealde.thread.lock.rw;

import com.sealde.thread.tool.SleepTool;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: sealde
 * @Date: 2019/9/27 下午4:16
 */
public class GoodsRwOps implements GoodsOps {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Goods goods;

    public GoodsRwOps(Goods goods) {
        this.goods = goods;
    }

    // 获取价格
    public double getPrice() {
        lock.readLock().lock();
        try {
            SleepTool.sleep(1);
            return this.goods.getPrice();
        } finally {
            lock.readLock().unlock();
        }
    }

    // 设置价格
    public void addPrice(double newPrice) {
        lock.writeLock().lock();
        try {
            SleepTool.sleep(5);
            this.goods.setPrice(this.goods.getPrice() + newPrice);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
