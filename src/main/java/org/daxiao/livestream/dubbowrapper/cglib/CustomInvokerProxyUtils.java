package org.daxiao.livestream.dubbowrapper.cglib;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
public class CustomInvokerProxyUtils {

    private static final AtomicInteger INC = new AtomicInteger();

//    public static void main(String[] args) throws Exception {
//        // 创建源对象（即被代理对象）
//        DemoFacadeImpl demoFacade = new DemoFacadeImpl();
//        // 生成自定义的代理类
//        CustomInvoker invoker = (CustomInvoker)CustomInvokerProxyUtils.newProxyInstance(demoFacade);
//        // 调用代理类的方法
//        invoker.invokeMethod(demoFacade, "sayHello", new Class[]{String.class}, new Object[]{"Geek"});
//    }

    // 创建源对象（被代理对象）的代理对象
    public static Object newProxyInstance(Object sourceTarget) throws Exception{
        String packageName = "org.daxiao.livestream.dubbowrapper.cglib";
        // filePath = /E:/工程所在的磁盘路径/dubbo-15-dubbo-wrapper/target/classes/com/hmilyylimh/cloud/wrapper/custom
        String filePath = CustomInvokerProxyUtils.class.getResource("/").getPath()
                + CustomInvokerProxyUtils.class.getPackage().toString().substring("package ".length()).replaceAll("\\.", "/");
//        Class<?> targetClazz = sourceTarget.getClass().getInterfaces()[0];
        Class<?> targetClazz = sourceTarget.getClass();

        // proxyClassName = $DemoFacadeCustomInvoker_1
        String proxyClassName = "$" + targetClazz.getSimpleName() + "CustomInvoker_" + INC.incrementAndGet();

        // 获取代理的字节码内容 拼出一个Java文件出来
        String proxyByteCode = getProxyByteCode(packageName, proxyClassName, targetClazz);

        // 缓存至磁盘中
        file2Disk(filePath, proxyClassName, proxyByteCode);

        // 等刷盘稳定后
        TimeUnit.SECONDS.sleep(2);

        // 再编译java加载class至内存中
        Object compiledClazz = compileJava2Class(filePath, packageName, proxyClassName, sourceTarget, targetClazz);

        // 返回实例化的对象
        return compiledClazz;
    }

    private static String getProxyByteCode(String packageName, String proxyClassName, Class<?> targetClazz) {
        StringBuilder sb = new StringBuilder();
        // pkgContent = package com.hmilyylimh.cloud.wrapper.custom;
        String pkgContent = "package " + packageName + ";";
        // importTargetClazz = import com.hmilyylimh.cloud.facade.demo.DemoFacade;
        String importTargetClazz = "import " + targetClazz.getName() + ";";
        // importNoSuchMethodException = import org.apache.dubbo.common.bytecode.NoSuchMethodException;
        String importNoSuchMethodException = "import " + org.apache.dubbo.common.bytecode.NoSuchMethodException.class.getName() + ";";
        // classHeadContent = public class $DemoFacadeCustomInvoker extends CustomInvoker {
        String classHeadContent = "public class " + proxyClassName + " extends " + CustomInvoker.class.getSimpleName() + " {" ;

        // 添加内容
        sb.append(pkgContent).append(importTargetClazz).append(importNoSuchMethodException).append(classHeadContent);

        // invokeMethodHeadContent = public Object invokeMethod(Object instance, String mtdName, Class<?>[] types, Object[] args) throws NoSuchMethodException {
        String invokeMethodHeadContent = "public " + Object.class.getName() + " invokeMethod" +
                "(" + Object.class.getName() + " instance, "
                + String.class.getName() + " mtdName, " + Class.class.getName() + "<?>[] types, "
                + Object.class.getName() + "[] args) throws " + org.apache.dubbo.common.bytecode.NoSuchMethodException.class.getName() + " {\n";
        sb.append(invokeMethodHeadContent);

        for (Method method : targetClazz.getDeclaredMethods()) {
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();

            // if ("sayHello".equals(mtdName)) {
            String ifHead = "if (\"" + methodName + "\".equals(mtdName)) {\n";
            // return ((DemoFacade) instance).sayHello(String.valueOf(args[0]));
            String ifContent = null;
            // 这里有 bug ，姑且就入参就传一个入参对象吧
            if(parameterTypes.length != 0){
                ifContent = "return ((" + targetClazz.getName() + ") instance)." + methodName + "(" + String.class.getName() + ".valueOf(args[0]));\n";
            } else {
                ifContent = "return ((" + targetClazz.getName() + ") instance)." + methodName + "();\n";
            }
            // }
            String ifTail = "}\n";

            sb.append(ifHead).append(ifContent).append(ifTail);
        }

        // throw new NoSuchMethodException("Method [" + mtdName + "] not found.");
        String invokeMethodTailContent = "throw new " + org.apache.dubbo.common.bytecode.NoSuchMethodException.class.getName() + "(\"Method [\" + mtdName + \"] not found.\");\n}\n";
        sb.append(invokeMethodTailContent);

        // 类的尾巴大括号
        String classTailContent = " } ";
        sb.append(classTailContent);

        return sb.toString();
    }

    private static void file2Disk(String filePath, String proxyClassName, String proxyByteCode) throws IOException {
        File file = new File(filePath + File.separator + proxyClassName + ".java");
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(proxyByteCode);
        fileWriter.flush();
        fileWriter.close();
    }

    private static Object compileJava2Class(String filePath, String packageName, String proxyClassName, Object argsTarget, Class<?> targetClazz) throws Exception {
        // 编译 Java 文件
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjects(new File(filePath + File.separator + proxyClassName + ".java"));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        task.call();
        fileManager.close();

        // 加载 class 文件
        URL[] urls = new URL[]{new URL("file:" + filePath)};
        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        Class<?> clazz = urlClassLoader.loadClass(packageName + "." + proxyClassName);
        // 反射创建对象，并且实例化对象
        Constructor<?> constructor = clazz.getConstructor();
        Object newInstance = constructor.newInstance();
        return newInstance;
    }
}