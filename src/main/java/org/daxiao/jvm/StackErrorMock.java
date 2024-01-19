package org.daxiao.jvm;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class StackErrorMock {

    private static int stackDeep = 0;
    private static void call() {
        stackDeep++;
        try {
            TimeUnit.SECONDS.sleep(1);
            call();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        try {
//            call();
//        } catch (Throwable e) {
//            e.printStackTrace();
//            System.out.println("stack deep:" + stackDeep);
//        }

        for (int i = 0; i < 1000000; i++) {
            new Thread(() -> {
                call();
            }).start();
        }
        TimeUnit.MINUTES.sleep(1);
    }
}
