package org.example.Custom_Panels;

import org.example.Config.CONFIG;
import org.example.Main_Package.*;

import java.awt.*;

import static org.example.Main_Package.Main.MyPlayerIndex;


public class CQuestPanel {
    public static void RenderQuest(Graphics2D graphics2D, int windowWidth, int windowHeight) {
        int newX = CONFIG.WINDOW_WIDTH - (windowWidth + 25);

        graphics2D.setColor(new Color(0,0,0,100));
        graphics2D.fillRect(newX,0,windowWidth,windowHeight);

        graphics2D.setColor(Color.WHITE);
        Font ContentFont = new Font("Comic Sans MS", Font.BOLD, 15);
        Font TitleFont = new Font("Comic Sans MS", Font.BOLD, 28);

        FontMetrics titleFontMetrics = graphics2D.getFontMetrics(TitleFont);
        FontMetrics contentFontMetrics = graphics2D.getFontMetrics(ContentFont);

        int titleCordX = newX + (windowWidth - titleFontMetrics.stringWidth("Active Quest")) / 2;

        graphics2D.setFont(TitleFont);
        graphics2D.drawString("Active Quest",titleCordX,titleFontMetrics.getHeight());

        graphics2D.setFont(ContentFont);
        graphics2D.setColor(Color.ORANGE);

        String tempString;
        int contentCordX;
        int skips = 0;

        for (int i = 0; i < 6; i++) {
            tempString = "";

            switch (i) {
                case 0 -> {
                    if (Main.activeQuestObject.targetStatKillCount == 0) {
                        skips++;
                        continue;
                    }
                    tempString = "Kill Enemies: " + Main.PlayerList.get(MyPlayerIndex).statKillCount + " / " + Main.activeQuestObject.targetStatKillCount;
                }
                case 1 -> {
                    if (Main.activeQuestObject.targetStatTrapCount == 0) {
                        skips++;
                        continue;
                    }
                    tempString = "Punch Traps: " + Main.PlayerList.get(MyPlayerIndex).statTrapCount + " / " + Main.activeQuestObject.targetStatTrapCount;
                }
                case 2 -> {
                    if (Main.activeQuestObject.targetStatBoosterCount == 0) {
                        skips++;
                        continue;
                    }
                    tempString = "Catch Boosters: " + Main.PlayerList.get(MyPlayerIndex).statBoosterCount + " / " + Main.activeQuestObject.targetStatBoosterCount;
                }
                case 3 -> {
                    if (Main.activeQuestObject.targetStatDamageTaken == 0) {
                        skips++;
                        continue;
                    }
                    tempString = "Take Damage: " + Main.PlayerList.get(MyPlayerIndex).statDamageTaken + " / " + Main.activeQuestObject.targetStatDamageTaken;
                }
                case 4 -> {
                    if (Main.activeQuestObject.targetStatDamageDealt == 0) {
                        skips++;
                        continue;
                    }
                    tempString = "Deal Damage: " + Main.PlayerList.get(MyPlayerIndex).statDamageDealt + " / " + Main.activeQuestObject.targetStatDamageDealt;
                }
                case 5 -> {
                    if (Main.activeQuestObject.targetStatShootCount == 0) {
                        skips++;
                        continue;
                    }
                    tempString = "Shoot Count: " + Main.PlayerList.get(MyPlayerIndex).statShootCount + " / " + Main.activeQuestObject.targetStatShootCount;
                }
            }
            contentCordX = newX + (windowWidth - contentFontMetrics.stringWidth(tempString)) / 2;
            graphics2D.drawString(tempString, contentCordX, 50 + contentFontMetrics.getHeight() * (2 * (i + 1 - skips)));
        }

        tempString = "Money: " + Main.activeQuestObject.rewardCash + "$";
        contentCordX = newX + (windowWidth - contentFontMetrics.stringWidth(tempString)) / 2;
        graphics2D.drawString(tempString, contentCordX, windowHeight - contentFontMetrics.getHeight() * 3);

        tempString = "Healing: " + Main.activeQuestObject.rewardHeal;
        contentCordX = newX + (windowWidth - contentFontMetrics.stringWidth(tempString)) / 2;
        graphics2D.drawString(tempString, contentCordX, windowHeight - contentFontMetrics.getHeight());

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(TitleFont);
        tempString = "Reward";
        contentCordX = newX + (windowWidth - titleFontMetrics.stringWidth(tempString)) / 2;
        graphics2D.drawString(tempString, contentCordX, windowHeight - contentFontMetrics.getHeight() * 5);


    }
}
