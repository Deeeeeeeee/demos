package com.sealde.thread.forkjoin;

import com.sealde.thread.tool.SleepTool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 使用 fork join 对数组求和
 * @Author: sealde
 * @Date: 2019/9/24 上午11:31
 */
public class CalSumDemo {
    private static final int ARRAY_SIZE = 10000;

    private static class MyTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = ARRAY_SIZE / 10;  // 阈值
        private int[] source;                       // 需要求和的数组
        private int left;
        private int right;

        public MyTask(int[] source, int left, int right) {
            this.source = source;
            this.left = left;
            this.right = right;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            if ((right - left) < THRESHOLD) {
//                SleepTool.sleep(1);
                for (int i = left; i <= right; i++) {
                    sum += source[i];
                }
            } else {
                int mid = (left + right) / 2;
                MyTask leftTask = new MyTask(source, left, mid);
                MyTask rightTask = new MyTask(source, mid + 1, right);
                leftTask.fork();
                rightTask.fork();
                sum = leftTask.join() + rightTask.join();
            }
            return sum;
        }
    }

    public static int[] generateArray(int end) {
        int[] result = new int[end];
        for (int i = 1; i <= end; i++) {
            result[i-1] = i;
        }
        return result;
    }

    public static void main(String[] args) {
        int[] source = generateArray(ARRAY_SIZE);
        ForkJoinPool pool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        MyTask myTask = new MyTask(source, 0, source.length-1);
        ForkJoinTask<Integer> task = pool.submit(myTask);
        System.out.println(task.join());
        System.out.println("耗时:" + (System.currentTimeMillis() - start) + "ms");
    }
}
