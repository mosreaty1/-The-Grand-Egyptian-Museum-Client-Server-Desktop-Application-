package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class AskQuestionFrame extends JFrame {

    private final JTextArea chatArea;

    public AskQuestionFrame() {
        setTitle("Ask a Question");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JTextField inputField = new JTextField();
        inputPanel.add(inputField, BorderLayout.CENTER);

        JButton askButton = createStyledButton("Ask", new Color(255, 128, 0));
        askButton.addActionListener(e -> {
            String userInput = inputField.getText();
            if (!userInput.isEmpty()) {
                appendChatMessage("You: " + userInput);
                inputField.setText("");
                respondToUserInput(userInput);
            }
        });
        inputPanel.add(askButton, BorderLayout.EAST);

        JButton backButton = createStyledButton("Back", new Color(255, 128, 0));
        backButton.addActionListener(e -> {
            new MainPage();
            dispose();
        });
        inputPanel.add(backButton, BorderLayout.WEST);

        backgroundPanel.add(inputPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(100, 40));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private void appendChatMessage(String message) {
        chatArea.append(message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void respondToUserInput(String userInput) {
        String response;
        if (userInput.equalsIgnoreCase("hello")) {
            response = "Hi there!";
        } else if (userInput.equalsIgnoreCase("how are you?")) {
            response = "I'm good, thank you. How about you?";
        } else if (userInput.equalsIgnoreCase("bye")) {
            response = "Goodbye! Have a great day!";
        } else {
            response = "Sorry, I didn't understand that.";
        }
        appendChatMessage("Chatbot: " + response);

        // Exit the application when "bye" is the input
        if (userInput.equalsIgnoreCase("bye")) {
            dispose();
        }
    }

    private static class BackgroundPanel extends JPanel {
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
        SwingUtilities.invokeLater(AskQuestionFrame::new);
    }
}
