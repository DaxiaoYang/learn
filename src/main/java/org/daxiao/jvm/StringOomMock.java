package org.daxiao.jvm;

import java.util.ArrayList;
import java.util.List;

public class StringOomMock {

    private static String base = "String";
    public static void main(String[] args) {
        List<String> bases = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String str = base + base;
            base = str;
            bases.add(str.intern());
        }
    }
}
