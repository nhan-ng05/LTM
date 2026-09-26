package bai5;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {

        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server đang chạy tại port " + port);

            while (true) {

                System.out.println("Đang chờ Client...");

                Socket socket = serverSocket.accept();

                System.out.println(
                        "Client kết nối: " +
                                socket.getInetAddress());

                // Tạo Thread xử lý Client
                Thread clientThread = new Thread(
                        new ClientHandler(socket));

                clientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}