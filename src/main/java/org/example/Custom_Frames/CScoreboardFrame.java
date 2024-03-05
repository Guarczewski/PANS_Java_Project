package org.example.Custom_Frames;

import org.example.Config.CONFIG;
import org.example.Custom_Elements.CButton;
import org.example.Custom_Elements.CLabel;
import org.example.Custom_Objects.SavedObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CScoreboardFrame extends JFrame {

    public CScoreboardFrame() {
        super("Full Scoreboard Frame");
        setBounds((CONFIG.WINDOW_WIDTH / 2) - (CONFIG.SCOREBOARD_WINDOW_WIDTH / 2), (CONFIG.WINDOW_HEIGHT / 2) - (CONFIG.SCOREBOARD_WINDOW_HEIGHT / 2), CONFIG.SCOREBOARD_WINDOW_WIDTH, CONFIG.SCOREBOARD_WINDOW_HEIGHT);

        setResizable(false);

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBackground(Color.DARK_GRAY);

        JPanel ScoreboardListPanel = new JPanel();
        ScoreboardListPanel.setLayout(new BoxLayout(ScoreboardListPanel, BoxLayout.Y_AXIS));
        ScoreboardListPanel.setBackground(Color.DARK_GRAY);

        JScrollPane scoreboardScroll = new JScrollPane(ScoreboardListPanel);
        scoreboardScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        List<SavedObject> savedObjectList = SavedObject.Read();

        for (int i = 0; i < savedObjectList.size(); i++) {
            ScoreboardListPanel.add(new CLabel(savedObjectList.get(i).toScoreboardString(i), 18,Color.ORANGE));
        }

        CButton closePortListFrameButton = new CButton("Close", 16, Color.RED, Color.WHITE);
        closePortListFrameButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dispose();
            }
        });

        jPanel.add(new CLabel("TOP PLAYER LIST", 32, Color.WHITE), BorderLayout.NORTH);
        jPanel.add(scoreboardScroll, BorderLayout.CENTER);
        jPanel.add(closePortListFrameButton, BorderLayout.SOUTH);

        setContentPane(jPanel);
        setVisible(true);

    }

    private void Dispose() {
        this.dispose();
    }
}