package org.daxiao.multithread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABAProblemDemo {

    public static void main(String[] args) {
        // ABA问题场景：就算值符合预期 但是需要察觉到值的变化
        // OPEN_OR_CLOSE 表示 保存高考卷的保险柜 开关，false表示close， true 表示 open，考虑下面的业务场景，
        // 高考卷的保险柜，有且仅有两人有打开的权限。根据保密要求，人越少越好。当然，有且仅有一个人有权限，保密性更高，但是如果这人发生意外，就没人能打开保险柜
        // 所以选两个人 既能照顾到保密性要求，又能减少突发事件的影响。
        // 要求 2022-06-07 06:00:00 后，两个线程竞争去开保险柜，有且只有一人能打开，打开保险柜的人负责护送试题。（不考虑 synchronized 的实现方式）
        // 假设这样的一种场景，张三、李四 竞争开柜的过程中，张三使手段让李四在开柜前，卡一下，确保自己能先开柜，然后拍照，获取试题，最后关上柜门
        // 这个时候李四来开柜门，发现门的状态和教育部说的状态一样，都是close，然后李四拿走试题，张三过来说“李四啊，这次送试卷的任务就只能麻烦你了。”
        // 然后 张三转手卖出试题，就算出了事情，教育厅也只能查李四。

        AtomicStampedReference<Integer> count = new AtomicStampedReference<>(0, 0);

        new Thread(() -> {
            int stamp = count.getStamp();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            boolean b = count.compareAndSet(0, 1, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + " value:" + count.getReference() + " stamp:" + count.getStamp() + " success:" + b);
        }).start();

        new Thread(() -> {
            boolean b = count.compareAndSet(0, 1, count.getStamp(), count.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + " value:" + count.getReference() + " stamp:" + count.getStamp() + " success:" + b);
            b = count.compareAndSet(1, 0, count.getStamp(), count.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + " value:" + count.getReference() + " stamp:" + count.getStamp() + " success:" + b);
        }).start();
    }
}
