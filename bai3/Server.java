package bai3;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {

        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server đang chạy tại port " + port);
            System.out.println("Đang chờ Client...");

            Socket socket = serverSocket.accept();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            String message;

            while ((message = in.readLine()) != null) {

                System.out.println("Client: " + message);

                String result = message.toUpperCase();

                out.println(result);
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
