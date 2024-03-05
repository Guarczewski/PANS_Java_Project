package org.example.Custom_Panels;

import org.example.Config.CONFIG;
import org.example.Custom_Objects.PurchaseOption;
import org.example.Main_Package.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.Main_Package.Main.MyPlayerIndex;

public class CShopPanel {
    public static List<PurchaseOption> ShopList = new ArrayList<>();
    public static Image shopkeeper = null;
    public static boolean shopInitialized = false;
    public static List <Rectangle> fakeButtons = new ArrayList<>();

    public static void RenderShop(Graphics2D graphics2D, int windowWidth, int windowHeight) {

        int newX = (CONFIG.WINDOW_WIDTH / 2) - (windowWidth/2);
        int newY = (CONFIG.WINDOW_HEIGHT / 2) - (windowHeight/2);

        graphics2D.setColor(new Color(0,0,0,100));
        graphics2D.fillRect(newX,newY,windowWidth,windowHeight);


        Font ContentFont = new Font("Comic Sans MS", Font.BOLD, 18);
        Font WalletFont = new Font("Comic Sans MS", Font.BOLD, 24);

        FontMetrics contentFontMetrics = graphics2D.getFontMetrics(ContentFont);
        int contentCordX_0, contentCordX_1, contentCordX_2;

        graphics2D.setColor(Color.GREEN);
        graphics2D.setFont(WalletFont);

        if (Main.PlayerList.contains(Main.playerHolder))
            graphics2D.drawString("Wallet: " + Main.PlayerList.get(MyPlayerIndex).wallet + "$", newX + 50, newY + 50);

        graphics2D.setFont(ContentFont);
        graphics2D.setColor(Color.WHITE);

        graphics2D.drawImage(shopkeeper,newX + windowWidth/2 - 200, newY,400,400,null);

        for (int i = 0; i < 10; i++) {
            if (i < ShopList.size()) {

                graphics2D.setColor(Color.WHITE);

                contentCordX_0 = newX + (windowWidth - contentFontMetrics.stringWidth(ShopList.get(i).optionName)) / 2;
                contentCordX_1 = newX + (windowWidth - contentFontMetrics.stringWidth(ShopList.get(i).optionPrice + "$")) / 2;
                contentCordX_2 = newX + (windowWidth - contentFontMetrics.stringWidth("Purchase Me")) / 2;

                graphics2D.drawString(ShopList.get(i).optionName, (int) (contentCordX_0 * 0.5), 375 + newY + contentFontMetrics.getHeight() * ((i + 1) * 2));
                graphics2D.drawString(ShopList.get(i).optionPrice + "$", contentCordX_1, 375 + newY + contentFontMetrics.getHeight() * ((i + 1) * 2));

                if (Main.PlayerList.get(Main.MyPlayerIndex).CanAfford(CONFIG.SHOP_PRICES[i]))
                    graphics2D.setColor(Color.GREEN);
                else
                    graphics2D.setColor(Color.RED);

                graphics2D.drawString("Purchase Me", (int) (contentCordX_2 * 1.5), 375 + newY + contentFontMetrics.getHeight() * ((i + 1) * 2));

                if (!shopInitialized) {
                    fakeButtons.add(new Rectangle((int) (contentCordX_2 * 1.5), (375 + newY + contentFontMetrics.getHeight() * ((i + 1) * 2)) - (int) (contentFontMetrics.getHeight()/1.5),contentFontMetrics.stringWidth("Purchase Me"),contentFontMetrics.getHeight()));
                }

            }
        }

        shopInitialized = true;

    }
    public static void UpdateShopList(){
        ShopList.clear();
        ShopList.add(new PurchaseOption("Shotgun",CONFIG.SHOP_PRICES[0]));
        ShopList.add(new PurchaseOption("Sniper Rifle",CONFIG.SHOP_PRICES[1]));
        ShopList.add(new PurchaseOption("Pistol",CONFIG.SHOP_PRICES[2]));
        ShopList.add(new PurchaseOption("Rifle",CONFIG.SHOP_PRICES[3]));
        ShopList.add(new PurchaseOption("Healing Potion",CONFIG.SHOP_PRICES[4]));
        ShopList.add(new PurchaseOption("Damage Upgrade",CONFIG.SHOP_PRICES[5]));

    }

}
