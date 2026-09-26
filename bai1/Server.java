package bai1;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {

        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server đang chạy tại port " + port);
            System.out.println("Đang chờ Client...");

            Socket socket = serverSocket.accept();

            System.out.println("Client đã kết nối!");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            // Đọc dữ liệu từ Client
            String message = in.readLine();

            System.out.println("Client gửi: " + message);

            // Gửi trả Client
            out.println("Server đã nhận: " + message);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}