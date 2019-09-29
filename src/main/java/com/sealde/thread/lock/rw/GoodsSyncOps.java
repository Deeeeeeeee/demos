package com.sealde.thread.lock.rw;

import com.sealde.thread.tool.SleepTool;

/**
 * @Author: sealde
 * @Date: 2019/9/27 下午4:30
 */
public class GoodsSyncOps implements GoodsOps {
    private Goods goods;

    public GoodsSyncOps(Goods goods) {
        this.goods = goods;
    }

    // 获取价格
    public synchronized double getPrice() {
        SleepTool.sleep(1);
        return this.goods.getPrice();
    }

    // 设置价格
    public synchronized void addPrice(double newPrice) {
        SleepTool.sleep(5);
        this.goods.setPrice(this.goods.getPrice() + newPrice);
    }
}
