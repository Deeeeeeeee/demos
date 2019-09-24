package com.sealde.thread.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 列出文件夹下的所有 java 文件
 * @Author: sealde
 * @Date: 2019/9/24 下午2:46
 */
public class ShowFileDemo {
    private static class MyTask extends RecursiveAction {
        private File source;

        public MyTask(File source) {
            this.source = source;
        }

        @Override
        protected void compute() {
            List<MyTask> myTaskList = new ArrayList<>();
            if (source == null) {
                return;
            }
            File[] files = source.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    myTaskList.add(new MyTask(f));
                } else {
                    String fileName = f.getName();
                    if (fileName.endsWith(".java")) {
                        System.out.println(fileName);
                    }
                }
            }
            // invokeAll 所有子任务，然后等待任务完成
            for (MyTask t : invokeAll(myTaskList)) {
                t.join();
            }
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        MyTask myTask = new MyTask(new File("/home/sealde/Documents/project/git/demos"));
        pool.submit(myTask);    // submit 是非阻塞方法
//        pool.invoke(myTask);    // invoke 是阻塞方法
        myTask.join();
        System.out.println("耗时:" + (System.currentTimeMillis() - start) + "ms");
    }
}
