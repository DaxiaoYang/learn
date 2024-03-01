package org.daxiao.livestream.dubbowrapper;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class InvokeCostDemo {

    private static RunDemoClass normalInvokeCache;
    private static RunDemoClass reflectInvokeCache;

    private static Class<?> aClassCache;

    private static Method methodCache;


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        aClassCache = Class.forName("org.daxiao.livestream.dubbowrapper.RunDemoClass");
        methodCache = aClassCache.getDeclaredMethod("doSomeThing");
        methodCache.setAccessible(true);
        reflectInvokeCache = (RunDemoClass) aClassCache.newInstance();
        normalInvokeCache = new RunDemoClass();
        int runTimes = 100000000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < runTimes; i++) {
            normalInvoke();
        }
        // normalInvoke time:4 不缓存实例 Class Method对象
        // normalInvoke time:3 缓存实例 Class Method对象
        log.info("normalInvoke time:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        for (int i = 0; i < runTimes; i++) {
            reflectInvoke();
        }
        // reflectInvoke time:522 不缓存实例 Class Method对象
        // reflectInvoke time:73 缓存实例 Class Method对象
        log.info("reflectInvoke time:{}", System.currentTimeMillis() - startTime);
    }

    public static void normalInvoke() {
        normalInvokeCache.doSomeThing();
    }

    public static void reflectInvoke() throws InvocationTargetException, IllegalAccessException {
        methodCache.invoke(reflectInvokeCache);
    }
}
