package com.sealde.thread.lock.rw;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: sealde
 * @Date: 2019/9/27 下午4:09
 */
public class ReadWriteDemo {
    private static class ReadThread extends Thread {
        private CountDownLatch latch;
        private GoodsOps goodsOps;

        public ReadThread(GoodsOps goodsOps, CountDownLatch latch) {
            this.goodsOps = goodsOps;
            this.latch = latch;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 500; i++) {
                goodsOps.getPrice();
            }
            System.out.println(String.format("读线程耗时【%d】", System.currentTimeMillis() - start));
            latch.countDown();
        }
    }

    private static class WriteThread extends Thread {
        private CountDownLatch latch;
        private GoodsOps goodsOps;

        public WriteThread(GoodsOps goodsOps, CountDownLatch latch) {
            this.goodsOps = goodsOps;
            this.latch = latch;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                goodsOps.addPrice(1.0);
            }
            System.out.println(String.format("写线程耗时【%d】", System.currentTimeMillis() - start));
            latch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(30);

        Goods goods = new Goods(20.0, "测试商品");
//        GoodsOps goodsOps = new GoodsRwOps(goods);
        GoodsOps goodsOps = new GoodsSyncOps(goods);
        for (int i = 0; i < 20; i++) {
            ReadThread t = new ReadThread(goodsOps, latch);
            t.start();
        }
        for (int i = 0; i < 10; i++) {
            WriteThread t = new WriteThread(goodsOps, latch);
            t.start();
        }

        latch.await();
        System.out.println(goods.getPrice());
    }
}
