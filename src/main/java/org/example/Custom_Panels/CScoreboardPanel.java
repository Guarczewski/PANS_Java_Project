package org.example.Custom_Panels;

import org.example.Config.CONFIG;
import org.example.Custom_Objects.SavedObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CScoreboardPanel {
    public static List<String> ScoreboardList = new ArrayList<>();
    public static void UpdateScoreboardList(){
        ScoreboardList.clear();
        List<SavedObject> savedObjectList = SavedObject.Read();
        for (int i = 0; i < savedObjectList.size(); i++) {
            ScoreboardList.add(savedObjectList.get(i).toScoreboardString(i));
        }
    }
    public static void RenderScoreboard(Graphics2D graphics2D, int windowWidth, int windowHeight) {
        int newX = (CONFIG.WINDOW_WIDTH / 2) - (windowWidth/2);
        int newY = (CONFIG.WINDOW_HEIGHT / 2) - (windowHeight/2);

        graphics2D.setColor(new Color(0,0,0,100));
        graphics2D.fillRect(newX,newY,windowWidth,windowHeight);

        graphics2D.setColor(Color.WHITE);
        Font ContentFont = new Font("Comic Sans MS", Font.BOLD, 24);
        Font TitleFont = new Font("Comic Sans MS", Font.BOLD, 94);

        FontMetrics titleFontMetrics = graphics2D.getFontMetrics(TitleFont);
        int titleCordX = newX + (windowWidth - titleFontMetrics.stringWidth("Top 10 Players")) / 2;

        FontMetrics contentFontMetrics = graphics2D.getFontMetrics(ContentFont);
        int contentCordX;

        graphics2D.setFont(TitleFont);
        graphics2D.drawString("Top 10 Players",titleCordX,newY);
        graphics2D.setFont(ContentFont);

        for (int i = 0; i < 10; i++) {
            if (i < ScoreboardList.size()) {
                contentCordX = newX + (windowWidth - contentFontMetrics.stringWidth(ScoreboardList.get(i))) / 2;
                graphics2D.drawString(ScoreboardList.get(i), contentCordX, newY + contentFontMetrics.getHeight() * ((i + 1) * 2));
            }
            else {
                break;
            }
        }
    }

}
