package org.daxiao.multithread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {

    private static int count = 0;

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new ReadTask()).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(new WriteTask()).start();
        }
    }

    private static class ReadTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                readLock.lock();
                System.out.println("read count:" + count);
                readLock.unlock();
            }
        }
    }

    private static class WriteTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                writeLock.lock();
                count++;
                System.out.println("write count:" + count);
                writeLock.unlock();
            }
        }
    }
}
