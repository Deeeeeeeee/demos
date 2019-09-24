package com.sealde.thread.tool.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * 数据库连接池
 * @Author: sealde
 * @Date: 2019/9/24 下午4:43
 */
public class DBPool {
    private final LinkedList<SqlConnection> pool = new LinkedList<>();
    // 可以使用连接
    private Semaphore canUsed = new Semaphore(20);
    // 已经使用连接
    private Semaphore hasUsed = new Semaphore(0);

    public DBPool() {
        for (int i = 0; i < 20; i++) {
            pool.addLast(new SqlConnection());
        }
    }

    public SqlConnection takeConn() throws InterruptedException {
        canUsed.acquire();
        SqlConnection conn;
        synchronized (pool) {
            conn = pool.removeFirst();
        }
        hasUsed.release();
        return conn;
    }

    public void releaseConn(SqlConnection conn) throws InterruptedException {
        if (conn == null) {
            return;
        }
        hasUsed.acquire();
        synchronized (pool) {
            pool.addLast(conn);
        }
        canUsed.release();
    }

    public static void main(String[] args) {
        DBPool pool = new DBPool();
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                long start = System.currentTimeMillis();
                try {
                    SqlConnection connect = pool.takeConn();
                    System.out.println("Thread_"+Thread.currentThread().getId()
                            +"_获取数据库连接共耗时【"+(System.currentTimeMillis()-start)+"】ms.");
                    connect.createStatement();
                    connect.commit();
                    System.out.println("查询数据完成，归还连接！");
                    pool.releaseConn(connect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
