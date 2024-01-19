package org.daxiao.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class DirectMemoryDemo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

//        ByteBuffer.allocateDirect(60 * 1024 * 1024);
        // 使用反射获取 Unsafe 类的字段 theUnsafe
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        unsafe.allocateMemory(1024 * 1024 * 1024);
//        compareAllocate();
//        compareWriteRead();

    }


    private static void compareAllocate() {
        int time = 10_000_000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            ByteBuffer.allocate(1);
        }
        System.out.println(System.currentTimeMillis() - start); // 21 这里直接申请的堆内存 只会触发缺页中断 不会申请内存的系统调用
        start = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            ByteBuffer.allocateDirect(1);
        }
        System.out.println(System.currentTimeMillis() - start); // 4360 系统调用太多了
    }

    private static void compareWriteRead() {
        // 为什么直接内存的 ByteBuffer 比堆上的 ByteBuffer 快
        /**
         * 这是 Unsafe 的功劳 。虽然 Java 不允许程序员直接操作指针和内存，但给标准库开了一个后门—— Unsafe。ByteBuffer.allocateDirect
         * 实际上就是通过 Unsafe 申请了一块直接内存，再把读写这块内存的操作封装为 ByteBuffer 的操作。
         * 把直接内存当数组用，就可以绕开 JVM 的数组越界检查，提高性能（存疑）。
         * 但这样做不是没有代价的，Unsafe 之所以叫 Unsafe 就是因为「不安全
         *
         * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
         */
        int time = 1000_000_000;
        long start = System.currentTimeMillis();
        ByteBuffer heapBuffer = ByteBuffer.allocate(2 * time);
        for (int i = 0; i < time; i++) {
            heapBuffer.putChar('a');
        }
        heapBuffer.flip();
        for (int i = 0; i < time; i++) {
            heapBuffer.getChar();
        }
        System.out.println(System.currentTimeMillis() - start); // 1227
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(2 * time);
        start = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            directBuffer.putChar('a');
        }
        directBuffer.flip();
        for (int i = 0; i < time; i++) {
            directBuffer.getChar();
        }
        System.out.println(System.currentTimeMillis() - start); // 224

        start = System.currentTimeMillis();
        char[] chars = new char[time];
        for (int i = 0; i < time; i++) {
            chars[i] = 'a';
        }
        directBuffer.flip();
        for (int i = 0; i < time; i++) {
            char c = chars[i];
        }
        System.out.println(System.currentTimeMillis() - start); // 3171
    }
}
