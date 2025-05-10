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

public class ChattingRoom extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JComboBox<String> statusComboBox;
    private String username, nationality;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private final Image backgroundImage = new ImageIcon("tourr.jpg").getImage();

    public ChattingRoom(String username, String nationality) {
        this.username = username;
        this.nationality = nationality;
        setTitle("Chatting Room - " + username + "( " + nationality + " )");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setOpaque(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());
        chatScrollPane.setOpaque(false);
        chatScrollPane.getViewport().setOpaque(false);

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setOpaque(false);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        backgroundPanel.add(chatPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setOpaque(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setOpaque(false);

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = createStyledButton("Send", new Color(245, 141, 56));
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        JButton saveButton = createStyledButton("Save Chat Log", new Color(245, 141, 56));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveChatLog();
            }
        });
        inputPanel.add(saveButton, BorderLayout.WEST);

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

        JButton closeButton = createStyledButton("Close Chat", Color.RED);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeConnection();
                new MainPage();
                dispose();
            }
        });
        statusPanel.add(closeButton);

        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        setVisible(true);
        connectToServer();
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 1234);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new IncomingReader()).start();
            out.println(username + " ( " + nationality + " )" + " has joined the chat.");
        } catch (IOException ex) {
            chatArea.append("Unable to connect to server.\n");
            ex.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(username + ": " + message);
            messageField.setText("");
        }
    }

    private void saveChatLog() {
        try {
            FileWriter writer = new FileWriter(username + "_chatlog.txt");
            writer.write(chatArea.getText());
            writer.close();
            JOptionPane.showMessageDialog(this, "Chat log saved!");
        } catch (IOException ex) {
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
                chatArea.append("Error receiving message.\n");
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
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
