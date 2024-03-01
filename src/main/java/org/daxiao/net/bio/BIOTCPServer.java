package org.daxiao.net.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOTCPServer {

    public static void main(String[] args) throws IOException {
        ServerSocket listenSocket = new ServerSocket(9999);
        while (true) {
            // 同步阻塞等待9999端口上的连接事件
            Socket connectSocket = listenSocket.accept();
            InputStream in = connectSocket.getInputStream();
            byte[] bytes = new byte[1024];
            // 同步阻塞等待连接成功的这个socket上的IO事件
            in.read(bytes);
            System.out.println("receive client " + new String(bytes).trim());
            connectSocket.getOutputStream().write("no way".getBytes());
            connectSocket.close();
        }
    }
}
