package org.daxiao.net.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestChat {

    public static void main(String[] args) throws IOException {
        NioChatClient client = new NioChatClient();
        new Thread(() -> {
            while (true) {
                try {
                    if (!client.receiveMsg()) {
                        log.info("client not receive any msg sleep for a while");
                        TimeUnit.SECONDS.sleep(2);
                    }
                } catch (Exception e) {
                    log.error("receive msg ", e);
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            client.sendMsg(msg);
        }
    }
}
