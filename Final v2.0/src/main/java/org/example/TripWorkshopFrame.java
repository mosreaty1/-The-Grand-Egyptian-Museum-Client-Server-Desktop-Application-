package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class TripWorkshopFrame extends JFrame {

    private final Timer timer;
    private int currentImageIndex;
    private static final String TRIP_WEBSITE_URL = "https://visit-gem.com/en/tours";

    public TripWorkshopFrame() {
        setTitle("Trip Workshop");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());


        JButton backButton = createStyledButton("Back", new Color(255, 128, 0));
        backButton.addActionListener(e -> {
            new MainPage();
            dispose();
        });

        JPanel sliderPanel = new JPanel() {
            private final Image[] images = {
                    new ImageIcon("BANNER1.jpg").getImage(),
                    new ImageIcon("BANNER2.JPG").getImage()
            };

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(images[currentImageIndex], 0, 0, getWidth(), getHeight(), this);
            }
        };
        sliderPanel.setPreferredSize(new Dimension(1000, 500));

        sliderPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(TRIP_WEBSITE_URL));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        timer = new Timer();
        currentImageIndex = 0;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    currentImageIndex = (currentImageIndex + 1) % 2;
                    sliderPanel.repaint();
                });
            }
        }, 0, 3000);


        backgroundPanel.add(sliderPanel, BorderLayout.CENTER);
        backgroundPanel.add(backButton, BorderLayout.SOUTH);

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
}
