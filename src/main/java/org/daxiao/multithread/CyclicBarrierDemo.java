package org.daxiao.multithread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙");
        });
        for (int i = 0; i < 7; i++) {
            int tempI = i;
            new Thread(() -> {
                for (int j = 0; j < 2; j++) {
                    System.out.println(Thread.currentThread().getName() + "拿到龙珠:" + tempI);
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + "的龙珠:" + tempI + "飞走了");
                }
            }, "第" + i +"人").start();
        }
    }
}
