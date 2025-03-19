package it203finalproject;

import javax.swing.*;
import java.awt.*;

public class BackgrounfPanel extends JPanel {
    private Image backgroundImage;

    // Constructor to initialize the background image
    public BackgrounfPanel(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage(); // nasa main class yung file path nilagay
    }

    // Override the paintComponent method to draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Calls the parent class's paintComponent to ensure proper painting
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // para ka sixe ng panel
    }
}
