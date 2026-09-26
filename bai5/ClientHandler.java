package bai5;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            String message;

            while ((message = in.readLine()) != null) {

                System.out.println(
                        "Client " +
                                socket.getInetAddress() +
                                ": " +
                                message);

                if (message.equalsIgnoreCase("exit")) {

                    out.println("Kết nối kết thúc.");

                    break;
                }

                String result = message.toUpperCase();

                out.println(result);
            }

        } catch (IOException e) {

            System.out.println(
                    "Client đã ngắt kết nối.");

        } finally {

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}