package org.example.Custom_Elements;

import javax.swing.*;
import java.awt.*;

public class CLabel extends JLabel {
    public CLabel(String text, int size){
        super(text, SwingConstants.CENTER);
        setFont(new Font("Comic Sans MS", Font.BOLD, size));
        setForeground(Color.BLACK);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
    }
    public CLabel(String text, int size, Color color){
        super(text, SwingConstants.CENTER);
        setFont(new Font("Comic Sans MS", Font.BOLD, size));
        setForeground(color);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
    }
    public CLabel(ImageIcon imageIcon){
        super(imageIcon);
    }
}
