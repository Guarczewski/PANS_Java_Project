package org.example.Custom_Elements;

import javax.swing.*;
import java.awt.*;

public class CButton extends JButton {
    public CButton(String text, int size, Color fontColor){
        super(text);
        setFont(new Font("Comic Sans MS", Font.BOLD, size));
        setForeground(fontColor);
        setBackground(new Color(0,0,0,0));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
    }
    public CButton(String text, int size, Color backgroundColor, Color foregroundColor){
        super(text);
        setFont(new Font("Comic Sans MS", Font.BOLD, size));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(backgroundColor);
        setForeground(foregroundColor);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
    }
}
