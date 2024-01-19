package org.daxiao.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HeapInstanceDemo {

    public static void main(String[] args) {
        // 空间担保分配
        List<byte[]> buffers = new ArrayList<>();
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            buffers.add(new byte[ThreadLocalRandom.current().nextInt(1024 * 1024)]);
//            byte[] bytes = new byte[ThreadLocalRandom.current().nextInt(1024 * 1024)];
        }
    }
}
