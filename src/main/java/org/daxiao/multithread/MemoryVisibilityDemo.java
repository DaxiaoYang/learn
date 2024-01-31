package org.daxiao.multithread;

public class MemoryVisibilityDemo {

    public static volatile boolean staticRun = true;

    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        new Thread(task).start();
        Thread.sleep(1000);
        task.run = false;
        System.out.println(Thread.currentThread().getName() + " run:" + task.run); // main run:false
    }

    public static class Task implements Runnable {

        public volatile boolean run = true;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " running");
            while (run) {
//                synchronized (this) {
//
//                }
            }
            System.out.println(Thread.currentThread().getName() + " ending"); // 不会走到这里
        }
    }
}
