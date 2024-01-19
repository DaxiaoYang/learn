package org.daxiao.jvm;

public class MemoryAllocationGuarantee {

    public static void main(String[] args) {
        // -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC
        int mb = 1024 * 1024;
        byte[] buffer1 = new byte[1 * mb];
        byte[] buffer2 = new byte[1 * mb];
        byte[] buffer3 = new byte[1 * mb];
        // 新生代:9M 不够新对象分配内存 触发内存担保 新生代中的老对象被移动到老年代
        byte[] buffer4 = new byte[7 * mb];
        System.out.println(MemoryAllocationGuarantee.class.toString());
    }
}
