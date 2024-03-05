package org.example.Custom_Elements;

import javax.swing.*;
import java.awt.*;

public class CTextField extends JTextField {
    public CTextField(String text, int size, Color fontColor){
        super(text);
        setFont(new Font("Comic Sans MS", Font.BOLD, size));
        setForeground(fontColor);
        setBackground(new Color(0,0,0,0));
        setHorizontalAlignment(JTextField.CENTER);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }
}
