package com.example.javacore.threads.virtual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GreetClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static GreetClient instance;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public String sendMessage(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static GreetClient getInstance() {
        if (instance == null)
            instance = new GreetClient();
        return instance;
    }

    public static void main(String[] args) throws Exception {
        GreetClient client = GreetClient.getInstance();
        client.startConnection("localhost", 1234);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.println("Message to server:");
        while ((line = br.readLine()) != null) {
            if (line.equals("bye")) {
                client.stopConnection();
                break;
            }
            String response = client.sendMessage(line);
            System.out.println("response from server: " + response);
            System.out.println("Message to server:");
        }
    }

}
