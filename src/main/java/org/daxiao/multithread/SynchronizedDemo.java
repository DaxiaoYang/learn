package org.daxiao.multithread;

public class SynchronizedDemo {

    public synchronized void syncMethod2() {
        System.out.println("sync");
    }

    public void syncMethod1() {
        synchronized (this) {
            System.out.println("sync");
        }
    }
}
