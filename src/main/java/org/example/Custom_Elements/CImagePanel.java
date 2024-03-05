package org.example.Custom_Elements;

import javax.swing.*;
import java.awt.*;

public class CImagePanel extends JPanel {
    private final Image image;
    private final int width, height;
    public CImagePanel(Image image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;

        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
    }
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0,width,height, null);
    }
}
