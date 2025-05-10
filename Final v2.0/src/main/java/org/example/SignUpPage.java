package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> nationalityField;
    private JComboBox<String> genderField;
    private final Image backgroundImage = new ImageIcon("formframe.jpg").getImage();
    private UserController userController;

    public SignUpPage() {
        userController = new UserController();

        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(30);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(30);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        confirmPasswordField = new JPasswordField(30);
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Nationality:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        String[] nationalities = {"Egyptian", "Saudi", "Jordanian", "Lebanese", "Syrian", "Other Arab"};
        nationalityField = new JComboBox<>(nationalities);
        formPanel.add(nationalityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Gender:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        String[] genders = {"Male", "Female"};
        genderField = new JComboBox<>(genders);
        formPanel.add(genderField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton signUpButton = createStyledButton("Sign Up");
        signUpButton.addActionListener(e -> signUp());
        buttonPanel.add(signUpButton);

        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        JButton loginButton = createStyledButton("Go to Login");
        loginButton.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
        buttonPanel.add(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        mainPanel.add(formPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonPanel, gbc);

        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        add(backgroundPanel);

        setVisible(true);
    }

    private void signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String nationality = (String) nationalityField.getSelectedItem();
        String gender = (String) genderField.getSelectedItem();

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userController.registerUser(username, password, nationality, gender);

        if (success) {
            JOptionPane.showMessageDialog(this, "Sign-Up Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginPage().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sign-Up Failed! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpPage::new);
    }
}
