package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer extends JFrame {
    private JTextArea logArea;
    private List<ClientHandler> clients;
    private UserController userController;

    public ChatServer() {
        userController = new UserController();
        setTitle("Chat Server");
        logArea = new JTextArea();
        logArea.setEditable(false);
        clients = new CopyOnWriteArrayList<>();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JScrollPane(logArea));
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        startServer();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            logArea.append("Server started on port 1234...\n");
            while (true) {
                Socket socket = serverSocket.accept();
                logArea.append("New client connected...\n");
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException ex) {
            logArea.append("Error starting the server: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ex) {
                logArea.append("Error initializing client handler: " + ex.getMessage() + "\n");
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    logArea.append("Received: " + message + "\n");
                    broadcastMessage(message);
                }
            } catch (IOException ex) {
                logArea.append("Error reading message: " + ex.getMessage() + "\n");
                ex.printStackTrace();
            } finally {
                try {
                    logArea.append("Client disconnected...\n");
                    socket.close();
                } catch (IOException ex) {
                    logArea.append("Error closing socket: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                }
                clients.remove(this);
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatServer::new);
    }
}
