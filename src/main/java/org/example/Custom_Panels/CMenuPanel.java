package org.example.Custom_Panels;

import org.example.Custom_Elements.CButton;
import org.example.Custom_Elements.CLabel;
import org.example.Custom_Elements.CTextField;

import javax.swing.*;
import java.awt.*;
public class CMenuPanel extends JPanel {
    public CButton StartGameButton, CloseGameButton, SettingsButton, OnlineConfigButton, FullScoreboardButton;
    public CTextField NickInput;
    public CMenuPanel() {

        this.setLayout(new GridLayout(8,0));
        this.setBackground(new Color(0,0,0,160));

        JPanel secondPartPanel = new JPanel(new GridLayout(2,0));
        JPanel thirdPartPanel = new JPanel(new GridLayout(2,0));
        JPanel fourthPartPanel = new JPanel(new GridLayout(2,0));
        JPanel fifthPartPanel = new JPanel(new GridLayout(2,0));

        secondPartPanel.setBackground(new Color(0,0,0,0));
        thirdPartPanel.setBackground(new Color(0,0,0,0));
        fourthPartPanel.setBackground(new Color(0,0,0,0));
        fifthPartPanel.setBackground(new Color(0,0,0,0));

        NickInput = new CTextField("Type Nick", 20,Color.ORANGE);
        SettingsButton = new CButton("<- Settings <-", 18,Color.ORANGE);
        OnlineConfigButton = new CButton("-> Multiplayer ->", 18,Color.ORANGE);
        FullScoreboardButton = new CButton("<- Scoreboard ->", 18,Color.ORANGE);

        secondPartPanel.add(new CLabel("Enter Nickname", 20,Color.WHITE));
        secondPartPanel.add(NickInput);

        thirdPartPanel.add(new CLabel("Settings", 20,Color.WHITE));
        thirdPartPanel.add(SettingsButton);

        fourthPartPanel.add(new CLabel("Online Configuration", 20,Color.WHITE));
        fourthPartPanel.add(OnlineConfigButton);

        fifthPartPanel.add(new CLabel("Open Full Scoreboard", 20,Color.WHITE));
        fifthPartPanel.add(FullScoreboardButton);

        JPanel MainMenuPanel = new JPanel(new GridLayout(2,2));

        MainMenuPanel.setBackground(new Color(0,0,0,0));

        StartGameButton = new CButton("Play", 16,Color.GREEN,Color.BLACK);
        CloseGameButton = new CButton("Close",16,Color.RED,Color.BLACK);

        MainMenuPanel.add(new CLabel("Start Game!", 20,Color.WHITE));
        MainMenuPanel.add(new CLabel("Close Game", 20,Color.WHITE));
        MainMenuPanel.add(StartGameButton);
        MainMenuPanel.add(CloseGameButton);

        this.add(new CLabel("Swing Shotgun Game!", 42,Color.WHITE));
        this.add(new JLabel());
        this.add(secondPartPanel);
        this.add(thirdPartPanel);
        this.add(fourthPartPanel);
        this.add(fifthPartPanel);
        this.add(new JLabel());
        this.add(MainMenuPanel);
    }
}
