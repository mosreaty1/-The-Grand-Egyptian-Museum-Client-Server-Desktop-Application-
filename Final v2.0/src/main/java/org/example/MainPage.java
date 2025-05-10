package org.example;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {

    public MainPage() {
        setTitle("Chat Application Main Page");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        JButton infoButton = createStyledButton("Info", new Color(255, 128, 0));
        JButton tripButton = createStyledButton("Trip Workshop", Color.GRAY);
        JButton askQuestionButton = createStyledButton("Ask Question", new Color(255, 128, 0));
        JButton loginButton = createStyledButton("Login", Color.GRAY);
        JButton signupButton = createStyledButton("Sign Up", Color.GRAY);

        infoButton.addActionListener(e -> openInfoFrame());
        tripButton.addActionListener(e -> openTripWorkshopFrame());
        askQuestionButton.addActionListener(e -> askQuestion());
        loginButton.addActionListener(e -> openLoginPage());
        signupButton.addActionListener(e -> openSignUpPage());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        buttonPanel.setOpaque(false);

        buttonPanel.add(infoButton);
        buttonPanel.add(tripButton);
        buttonPanel.add(askQuestionButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        backgroundPanel.add(Box.createVerticalStrut(270));
        backgroundPanel.add(buttonPanel);

        add(backgroundPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private void openInfoFrame() {
        new InfoFrame();
        dispose();
    }

    private void openTripWorkshopFrame() {
        new TripWorkshopFrame();
        dispose();
    }

    private void askQuestion() {
        new AskQuestionFrame();
        dispose();
    }

    private void openLoginPage() {
        new LoginPage();
        dispose();
    }

    private void openSignUpPage() {
        new SignUpPage();
        dispose();
    }

    private static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("home (1).png").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainPage::new);
    }
}
