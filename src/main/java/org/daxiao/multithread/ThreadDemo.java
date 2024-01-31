package org.daxiao.multithread;

public class ThreadDemo {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("thread start");
        });
        thread.start();
    }
}
