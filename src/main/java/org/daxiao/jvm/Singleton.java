package org.daxiao.jvm;

public class Singleton {

    // 私有构造函数，防止外部实例化
    private Singleton() {}

    static {
        System.out.println("Singleton loaded");
    }

    // 内部静态类持有单例实例
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    // 公共静态方法获取单例实例
    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}