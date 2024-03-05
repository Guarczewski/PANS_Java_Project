package org.example.Custom_Panels;

import org.example.Config.CONFIG;

import org.example.Custom_Enums.ObjectType;
import org.example.Custom_Objects.*;

import org.example.Main_Package.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.Custom_Panels.CShopPanel.*;

public class CPanel extends JPanel {
    protected List<GameObject> tempDrawingList;
    Image backgroundImage;
    public CPanel(Image backgroundImage){
        this.setLayout(null);
        this.backgroundImage = backgroundImage;
        tempDrawingList = new ArrayList<>();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(backgroundImage,0,0,CONFIG.WINDOW_WIDTH,CONFIG.WINDOW_HEIGHT,this);

        tempDrawingList.clear();
        tempDrawingList.addAll(Main.PlayerList);
        tempDrawingList.addAll(Main.BulletList);
        tempDrawingList.addAll(Main.EnemyList);
        tempDrawingList.addAll(Main.BoosterList);
        tempDrawingList.addAll(Main.TrapList);

        setFont(new Font("Comic Sans MS", Font.BOLD, 11));

        for (GameObject gameObject : tempDrawingList) {
            if (gameObject.visible) {
                if (gameObject.myType == ObjectType.ENEMY || gameObject.myType == ObjectType.PLAYER) {
                    g2D.setColor(Color.WHITE);
                    g2D.drawString(gameObject.objectName, (int) gameObject.cordX, (int) gameObject.cordY - 28);
                    g2D.setColor(Color.RED);
                    g2D.fillRect((int) gameObject.cordX, (int) gameObject.cordY - 20, 100, 20);
                    g2D.setColor(Color.GREEN);
                    g2D.fillRect((int) gameObject.cordX, (int) gameObject.cordY - 20, (int) gameObject.hpCurrent, 20);
                }

                if (CONFIG.SIMPLE_SHAPES)
                    g2D.fillRect((int) gameObject.cordX, (int) gameObject.cordY,20,20);
                else {
                    gameObject.myCPolygon.Render(g2D, gameObject.MyColor);
                }
            }
        }

        if (Main.GAME_RUNNING)
            if (Main.PlayerList.contains(Main.playerHolder))
                CQuestPanel.RenderQuest(g2D,275,525);

        if (Main.displayScoreboard)
            if (Main.PlayerList.contains(Main.playerHolder))
                CScoreboardPanel.RenderScoreboard(g2D,1280,720);

        if (Main.displayPlayerList)
            if (Main.PlayerList.contains(Main.playerHolder))
                CPlayerList.RenderPlayerList(g2D,1280,720);

        if (Main.displayShop)
            if (Main.PlayerList.contains(Main.playerHolder))
                RenderShop(g2D,1280,720);


    }
}
