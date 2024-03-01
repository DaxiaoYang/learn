package org.daxiao;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;



@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("info");
        log.warn("warn");
        log.error("error");
        System.out.println("demoMethod".hashCode());
    }
}