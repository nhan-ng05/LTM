package bai4;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 5000)) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));

            while (true) {

                System.out.print("Client: ");

                String message = keyboard.readLine();

                out.println(message);

                String response = in.readLine();

                System.out.println("Server: " + response);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client kết thúc.");
    }
}
