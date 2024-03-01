package org.daxiao.net.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BIOTCPClient {

    public static void main(String[] args) throws IOException {
        while (true) {
            Socket socket = new Socket("127.0.0.1", 9999);
            OutputStream out = socket.getOutputStream();
            System.out.println("enter");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            out.write(msg.getBytes());
            InputStream in = socket.getInputStream();
            byte[] bytes = new byte[1024];
            in.read(bytes);
            System.out.println("client receive " + new String(bytes).trim());
            socket.close();
        }
    }
}
