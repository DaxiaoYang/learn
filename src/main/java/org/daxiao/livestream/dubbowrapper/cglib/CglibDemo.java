package org.daxiao.livestream.dubbowrapper.cglib;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class CglibDemo {

    public static void main(String[] args) throws Exception {
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\study\\Java-advance\\JavaCode\\JavaCode\\target");
//        TargetObject targetObject = new TargetObject();
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(TargetObject.class);
//        enhancer.setCallback(new MethodInterceptorImpl());
//        TargetObject proxy = (TargetObject) enhancer.create();
//        proxy.demoMethod();

        //        // 创建源对象（即被代理对象）
        TargetObject targetObject = new TargetObject();
        // 生成自定义的代理类
        CustomInvoker invoker = (CustomInvoker)CustomInvokerProxyUtils.newProxyInstance(targetObject);
        // 调用代理类的方法
//        invoker.invokeMethod(targetObject, "demoMethod", new Class[]{Void.class}, new Object[]{"Geek"});
        invoker.invokeMethod(targetObject, "demoMethod", new Class[]{System.class}, new Object[]{"daxiao"});
    }


    public static class MethodInterceptorImpl implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            log.info("before method is called");
            Object res = methodProxy.invokeSuper(o, objects);
            log.info("after method is called");
            return res;
        }
    }
}
