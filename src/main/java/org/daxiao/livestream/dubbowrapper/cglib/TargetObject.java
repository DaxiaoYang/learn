package org.daxiao.livestream.dubbowrapper.cglib;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TargetObject {

    public String demoMethod(String msg) {
        log.info("targetObject msg:{}", msg);
        return msg;
    }
}
