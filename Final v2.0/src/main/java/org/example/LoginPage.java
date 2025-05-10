package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserController userController;

    public LoginPage() {
        userController = new UserController();

        setTitle("Login");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setOpaque(false);

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10);
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), formGbc);

        formGbc.gridx = 1;
        formGbc.gridy = 0;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, formGbc);

        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), formGbc);

        formGbc.gridx = 1;
        formGbc.gridy = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, formGbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(e -> login());

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> {
            new MainPage();
            dispose();
        });
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(formPanel, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(buttonPanel, gbc);

        add(backgroundPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(114, 95, 78));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (userController.authenticateUser(username, password)) {

            Thread serverThread = new Thread(() -> {
                ChatServer chatServer = new ChatServer();
                chatServer.setVisible(true);
            });
            serverThread.start();

            SwingUtilities.invokeLater(() -> {

                ChattingRoom chattingRoom = new ChattingRoom(username, userController.getNationality());
                chattingRoom.setVisible(true);
                SecondaryChatRoom secondaryChatRoom = new SecondaryChatRoom(username);
                secondaryChatRoom.setVisible(true);
            });

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
        }
    }

    private static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("formframe.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
