package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SecondaryChatRoom extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JComboBox<String> statusComboBox;
    private String username;
    private PrintWriter out;
    private BufferedReader in;

    public SecondaryChatRoom(String username) {
        this.username = username;
        setTitle("Secondary Chat Room - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);


        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());


        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());

        backgroundPanel.add(chatScrollPane, BorderLayout.CENTER);


        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setOpaque(false);


        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);

        String[] statuses = {"Available", "Busy"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setPreferredSize(new Dimension(150, 30));
        statusComboBox.setBackground(new Color(217, 217, 217));
        statusComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        statusComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStatus();
            }
        });
        statusPanel.add(statusComboBox);


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setOpaque(false);

        messageField = new JTextField(30);
        messageField.setPreferredSize(new Dimension(400, 30));
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton sendButton = createStyledButton("Send", new Color(248, 120, 28));
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JButton saveButton = createStyledButton("Save Chat Log", new Color(248, 120, 28));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveChatLog();
            }
        });

        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        inputPanel.add(saveButton);


        messagePanel.add(statusPanel, BorderLayout.NORTH);
        messagePanel.add(inputPanel, BorderLayout.SOUTH);


        backgroundPanel.add(messagePanel, BorderLayout.SOUTH);


        add(backgroundPanel);

        connectToServer();
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 30));
        button.setBorder(BorderFactory.createLineBorder(backgroundColor.darker(), 2));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 1234);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new IncomingReader()).start();
            out.println(username + " has joined the secondary chat room.");
        } catch (IOException ex) {
            chatArea.append("Unable to connect to server.\n");
            ex.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.trim().isEmpty()) {
            out.println(username + " from secondary chat: " + message);
            messageField.setText("");
        }
    }

    private void saveChatLog() {
        try (FileWriter fileWriter = new FileWriter(username + "_secondary_chat_log.txt", true)) {
            fileWriter.write(chatArea.getText());
            JOptionPane.showMessageDialog(this, "Chat log saved!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving chat log: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateStatus() {
        String selectedStatus = (String) statusComboBox.getSelectedItem();
        if (selectedStatus != null) {
            out.println(username + " is now " + selectedStatus);
            chatArea.append(username + " is now " + selectedStatus + "\n");
        }
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    chatArea.append(message + "\n");
                }
            } catch (IOException ex) {
                chatArea.append("Connection lost.\n");
                ex.printStackTrace();
            }
        }
    }

    private class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("tourr.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SecondaryChatRoom("User").setVisible(true));
    }
}
