package org.example.Custom_Frames;

import org.example.Config.CONFIG;
import org.example.Custom_Elements.CButton;
import org.example.Custom_Elements.CLabel;
import org.example.Online_Modules.HostModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CPortListFrame extends JFrame {
    public CPortListFrame(int portSearchStart, int portSearchEnd){
        super("Available Port List");
        setBounds((CONFIG.WINDOW_WIDTH / 2)- (CONFIG.PORT_WINDOW_WIDTH / 2),(CONFIG.WINDOW_HEIGHT / 2) - (CONFIG.PORT_WINDOW_HEIGHT / 2), CONFIG.PORT_WINDOW_WIDTH, CONFIG.PORT_WINDOW_HEIGHT);

        setResizable(false);

        CONFIG.PORT_SEARCH_MIN = portSearchStart;
        CONFIG.PORT_SEARCH_MAX = portSearchEnd;

        JPanel jPanel = new JPanel(new BorderLayout());

        JPanel subJPanel = new JPanel(new GridLayout(0,2));
        subJPanel.add(new CLabel("Occupied Ports",30,Color.WHITE));
        subJPanel.add(new CLabel("Available Ports",30,Color.WHITE));
        subJPanel.setBackground(Color.DARK_GRAY);

        JPanel Lists = new JPanel(new GridLayout(0,2));

        JPanel OnlinePortList = new JPanel();
        JPanel OfflinePortList = new JPanel();

        OnlinePortList.setLayout(new BoxLayout(OnlinePortList,BoxLayout.Y_AXIS));
        OfflinePortList.setLayout(new BoxLayout(OfflinePortList,BoxLayout.Y_AXIS));

        OnlinePortList.setBackground(Color.DARK_GRAY);
        OfflinePortList.setBackground(Color.DARK_GRAY);

        JScrollPane jScrollPaneOnline = new JScrollPane(OnlinePortList);
        JScrollPane jScrollPaneOffline = new JScrollPane(OfflinePortList);

        jScrollPaneOnline.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPaneOffline.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        for (int i = CONFIG.PORT_SEARCH_MIN; i < CONFIG.PORT_SEARCH_MAX; i++) {
            if (HostModule.Available(i)) {
                OfflinePortList.add(new CLabel(i + " is FREE", 16, Color.GREEN));
            }
            else {
                OnlinePortList.add(new CLabel(i + " is TAKEN", 16, Color.RED));
            }
        }

        Lists.add(jScrollPaneOnline);
        Lists.add(jScrollPaneOffline);

        CButton closePortListFrameButton = new CButton("Close", 16, Color.RED, Color.WHITE);
        closePortListFrameButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dispose();
            }
        });

        jPanel.add(subJPanel,BorderLayout.NORTH);
        jPanel.add(Lists,BorderLayout.CENTER);
        jPanel.add(closePortListFrameButton,BorderLayout.SOUTH);

        setContentPane(jPanel);
        setVisible(true);

    }

    private void Dispose(){this.dispose();}

}
