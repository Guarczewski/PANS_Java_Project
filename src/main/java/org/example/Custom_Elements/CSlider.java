package org.example.Custom_Elements;

import javax.swing.*;
import java.awt.*;

public class CSlider extends JSlider {
    public CSlider(){
        setMinorTickSpacing(2);
        setMajorTickSpacing(10);
        setValue(100);
        setPaintTicks(true);
        setPaintLabels(true);
        setBackground(new Color(0,0,0,0));
        setForeground(Color.WHITE);
        setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
    }
}
