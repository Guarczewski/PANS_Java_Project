package org.example.Custom_Panels;

import org.example.Custom_Elements.CButton;
import org.example.Custom_Elements.CLabel;
import org.example.Custom_Elements.CTextField;

import javax.swing.*;
import java.awt.*;

public class CConnectionPanel extends JPanel {
    public boolean visible = false;
    public CButton JoinServerButton, StartSearchButton;
    public CTextField PortInput, AddressInput, PortSearchStartInput, PortSearchEndInput;
    public CConnectionPanel() {

        setLayout(new GridLayout(9,0));
        setBackground(new Color(0,0,0,100));

        JPanel OnlineConfigurationPanel = new JPanel(new GridLayout(6,0));
        OnlineConfigurationPanel.setBackground(new Color(0,0,0,0));

        AddressInput = new CTextField("192.168.1.2",22, Color.WHITE);
        PortInput = new CTextField("7777",22, Color.WHITE);

        PortSearchStartInput = new CTextField("7000",22, Color.WHITE);
        PortSearchEndInput = new CTextField("8000",22, Color.WHITE);

        JoinServerButton = new CButton("Join Server", 28,Color.GREEN,Color.BLACK);
        StartSearchButton = new CButton("Start Search", 28,Color.ORANGE,Color.BLACK);

        this.add(new CLabel("IP Input", 28,Color.WHITE));
        this.add(AddressInput);
        this.add(new CLabel("Port Input:", 28,Color.WHITE));
        this.add(PortInput);
        this.add(new CLabel("Action:", 28,Color.WHITE));
        this.add(JoinServerButton);
        this.add(new CLabel("Search Ports:", 28,Color.WHITE));

        JPanel jPanel = new JPanel(new GridLayout(2,2));
        jPanel.setBackground(new Color(0,0,0,0));
        jPanel.add(this.add(new CLabel("Search Start", 28,Color.WHITE)));
        jPanel.add(this.add(new CLabel("Search End", 28,Color.WHITE)));
        jPanel.add(PortSearchStartInput);
        jPanel.add(PortSearchEndInput);

        this.add(jPanel);
        this.add(StartSearchButton);

    }
}
