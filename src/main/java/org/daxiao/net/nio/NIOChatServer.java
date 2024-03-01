package org.daxiao.net.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

@Slf4j
public class NIOChatServer {

    private ServerSocketChannel listenSocket;

    private Selector selector;

    private int port = 9999;

    public NIOChatServer() {
        try {
            listenSocket = ServerSocketChannel.open();
            listenSocket.bind(new InetSocketAddress(port));
            listenSocket.configureBlocking(false);
            selector = Selector.open();
            listenSocket.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                if (selector.select(2000) == 0) {
                    log.info("server not detect any event");
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 处理连接事件
                    if (key.isAcceptable()) {
                        SocketChannel connectSocket = listenSocket.accept();
                        connectSocket.configureBlocking(false);
                        connectSocket.register(selector, SelectionKey.OP_READ);
                        log.info("client:{} up", connectSocket.getRemoteAddress().toString());
                    }
                    // 处理可读事件
                    if (key.isReadable()) {
                        processReadableEvent(key);
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            log.error("nioserver error", e);
        }
    }

    private void processReadableEvent(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        if (read > 0) {
            String msg = new String(buffer.array());
            log.info("server received :{}", msg);
            // 找到所有其他tcp连接 发送消息
            broadcastMsg(channel, msg);
        }
    }

    private void broadcastMsg(SocketChannel channel, String msg) throws IOException {
        for (SelectionKey selectionKey : selector.keys()) {
            Channel targetChannel = selectionKey.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != channel) {
                SocketChannel destChannel = (SocketChannel) targetChannel;
                ByteBuffer writeBuffer = ByteBuffer.wrap(msg.getBytes());
                destChannel.write(writeBuffer);
            }
        }
    }

    public static void main(String[] args) {
        NIOChatServer nioChatServer = new NIOChatServer();
        nioChatServer.start();
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNextLine()) {
//            String msg = scanner.nextLine();
//            if ("byte".equalsIgnoreCase(msg)) {
//                break;
//            }
//        }
    }
}
