package bai2;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port)) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            // Nhập từ bàn phím
            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));

            System.out.print("Nhập thông điệp: ");

            String message = keyboard.readLine();

            // Gửi Server
            out.println(message);

            // Nhận phản hồi
            String response = in.readLine();

            System.out.println("Server: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}