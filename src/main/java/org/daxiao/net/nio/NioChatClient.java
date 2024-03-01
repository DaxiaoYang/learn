package org.daxiao.net.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class NioChatClient {

    private String host = "127.0.0.1";

    private int port = 9999;

    private SocketChannel socketChannel;

    private String userName;

    public NioChatClient() throws IOException {
        socketChannel = SocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(host, port);
        boolean connect = socketChannel.connect(address);
        if (!connect) {
            System.out.println("connect failed");
            return;
        }
        socketChannel.configureBlocking(false);
        userName = socketChannel.getLocalAddress().toString().substring(1);
        log.info("client:{} is ready", userName);
    }

    public void sendMsg(String msg) throws IOException {
        if ("bye".equalsIgnoreCase(msg)) {
            socketChannel.close();
            return;
        }
        msg = userName + " say:" + msg;
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

    public boolean receiveMsg() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(buffer);
        if (read > 0) {
            String s = new String(buffer.array());
            log.info("userName:{} received:{}", userName, s.trim());
            return true;
        }
        return false;
    }
}
