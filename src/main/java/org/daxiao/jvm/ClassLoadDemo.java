package org.daxiao.jvm;

public class ClassLoadDemo {

    public static int number = 1;

    public static void print() {
        System.out.println("ClassLoadDemo print");
    }


    static {
        System.out.println("ClassLoadDemo loaded");
    }

    public static void main(String[] args) {
//        ClassLoadDemo classLoadDemo = new ClassLoadDemo();
//        System.out.println(ClassLoadDemo.number);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Singleton.getInstance());
            }).start();
        }
    }
}
