package org.example.Custom_Panels;

import org.example.Config.CONFIG;
import org.example.Custom_Objects.GameObject;
import org.example.Main_Package.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CPlayerList {
    public static List<String> PlayerList = new ArrayList<>();

    public static void UpdatePlayerList(){
        PlayerList.clear();
        List<GameObject> gameObjectList = new ArrayList<>();
        gameObjectList.addAll(Main.PlayerList);
        gameObjectList.addAll(Main.EnemyList);

        for (GameObject temp : gameObjectList)
            PlayerList.add(temp.objectName + " Weapon: " + temp.myWeapon + " DMG: " + temp.damage + " K: " + temp.statKillCount + " T: " + temp.statTrapCount + " B: " + temp.statBoosterCount);
    }

    public static void RenderPlayerList(Graphics2D graphics2D, int windowWidth, int windowHeight) {
        int newX = (CONFIG.WINDOW_WIDTH / 2) - (windowWidth/2);
        int newY = (CONFIG.WINDOW_HEIGHT / 2) - (windowHeight/2);

        graphics2D.setColor(new Color(0,0,0,100));
        graphics2D.fillRect(newX,newY,windowWidth,windowHeight);

        graphics2D.setColor(Color.WHITE);
        Font ContentFont = new Font("Comic Sans MS", Font.BOLD, 12);
        Font TitleFont = new Font("Comic Sans MS", Font.BOLD, 94);

        FontMetrics titleFontMetrics = graphics2D.getFontMetrics(TitleFont);
        int titleCordX = newX + (windowWidth - titleFontMetrics.stringWidth("Players Online")) / 2;

        FontMetrics contentFontMetrics = graphics2D.getFontMetrics(ContentFont);
        int contentCordX;

        graphics2D.setFont(TitleFont);
        graphics2D.drawString("Players Online",titleCordX,newY);

        graphics2D.setFont(ContentFont);

        for (int i = 0; i < 20; i++) {
            if (i < PlayerList.size()) {
                contentCordX = newX + (windowWidth - contentFontMetrics.stringWidth(PlayerList.get(i))) / 2;
                graphics2D.drawString(PlayerList.get(i), contentCordX, newY + contentFontMetrics.getHeight() * ((i + 1) * 2));
            }
            else {
                break;
            }
        }

    }

}
