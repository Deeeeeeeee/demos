package com.sealde.thread.lock.rw;

/**
 * @Author: sealde
 * @Date: 2019/9/27 下午4:12
 */
public class Goods {
    private double price;
    private String name;

    public Goods(double price, String name) {
        this.price = price;
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
