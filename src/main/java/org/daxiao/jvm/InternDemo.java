package org.daxiao.jvm;

public class InternDemo {

    public static void main(String[] args) {
        String aStringPool = "a";
        String aHeap = new String("a");
        String aStringPoolRef = aHeap.intern();
        System.out.println(aStringPoolRef == aStringPool); // true
    }
}
