package org.daxiao.jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceTypeDemo {

    public static void main(String[] args) {
        Object strongRef = new Object();

        // 内存不足时回收
        byte[] bytes1 = new byte[1 * 1024 * 1024];
        byte[] bytes2 = new byte[1 * 1024 * 1024];
        byte[] bytes3 = new byte[1 * 1024 * 1024];
        // 3M在新生代

        // 8M在老年代
        SoftReference<byte[]> softReference = new SoftReference<>(new byte[8 * 1024 * 1024]);

//        bytes1 = null;
//        bytes2 = null;
//        bytes3 = null;
//        System.gc();
        // 新生代放不下 触发young gc young gc后还是放不下 触发fullGC  fullGC中回收软引用指向的对象
        byte[] bytes4 = new byte[7 * 1024 * 1024];
        System.out.println("softReference" + softReference.get());

        // gc时回收
        WeakReference<Object> weakReference = new WeakReference<>(new Object());
        System.gc();
        System.out.println("weakReference" + weakReference.get());

        // 虚引用
        PhantomReference<Object> phantomReference = new PhantomReference<>(new Object(), new ReferenceQueue<>());
        System.out.println("phantomReference" + phantomReference.get());
        System.out.println(phantomReference.isEnqueued());
    }
}
