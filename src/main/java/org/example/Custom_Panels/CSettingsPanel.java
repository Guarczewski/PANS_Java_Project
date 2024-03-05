package org.example.Custom_Panels;

import org.example.Custom_Elements.CButton;
import org.example.Custom_Elements.CLabel;
import org.example.Custom_Elements.CSlider;

import javax.swing.*;
import java.awt.*;

public class CSettingsPanel extends JPanel {
    public boolean visible = false;
    public CButton AnimationButton, SimplerShapesButton;
    public CSlider backgroundVolumeSlider, shootingVolumeSlider;
    public CSettingsPanel() {

        JPanel volumeSettingPanel = new JPanel(new GridLayout(4,0));
        volumeSettingPanel.setBackground(new Color(0,0,0,0));

        backgroundVolumeSlider = new CSlider();
        shootingVolumeSlider = new CSlider();

        volumeSettingPanel.add(new CLabel("Change Background Volume",20,Color.WHITE));
        volumeSettingPanel.add(backgroundVolumeSlider);
        volumeSettingPanel.add(new CLabel("Change Shooting Sound Volume",20,Color.WHITE));
        volumeSettingPanel.add(shootingVolumeSlider);

        JPanel LocalConfigurationPanel = new JPanel(new GridLayout(4,0));
        LocalConfigurationPanel.setBackground(new Color(0,0,0,0));

        AnimationButton = new CButton("Turn Off Animations",18,Color.GREEN,Color.BLACK);
        SimplerShapesButton = new CButton("Turn On Simple Shapes",18, Color.RED,Color.BLACK);

        LocalConfigurationPanel.add(new CLabel("Turn Off / On Animations:", 20,Color.WHITE));
        LocalConfigurationPanel.add(AnimationButton);
        LocalConfigurationPanel.add(new CLabel("Turn Off / On Simple Shapes:", 20,Color.WHITE));
        LocalConfigurationPanel.add(SimplerShapesButton);

        setLayout(new GridLayout(3,0));
        setBackground(new Color(0,0,0,100));
        add(volumeSettingPanel);
        add(LocalConfigurationPanel);
        add(new JLabel());
    }
}