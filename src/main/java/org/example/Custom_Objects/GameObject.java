package org.example.Custom_Objects;

import org.example.Config.CONFIG;
import org.example.Custom_Elements.Sound;
import org.example.Custom_Enums.*;
import org.example.Custom_Polygons.CPolygon;
import org.example.Main_Package.Main;
import org.example.Online_Modules.*;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GameObject implements Serializable {
    public final UUID srcUUID;
    public final UUID myUUID = UUID.randomUUID();
    public final ObjectType srcObjectType;
    public CPolygon myCPolygon;
    public Color MyColor;
    public int MyScore, MyIndexInGlobalList, SrcIndexInGlobalList;
    public int statKillCount, statTrapCount, statBoosterCount, statDamageTaken, statDamageDealt, statShootCount;
    public double cordX,cordY, velY,velX;
    public double damage, hpCurrent, reloadTime;
    public ObjectType myType;
    public int rotation = 5;
    public int wallet = 1000000000;
    public String objectName;
    public WeaponType myWeapon;
    public boolean visible = true;

    public GameObject(double cordX, double cordY, double damage, double hpCurrent, ObjectType myType, String objectName, double velX, double velY, UUID srcUUID, ObjectType srcObjectType, int SrcIndexInGlobalList){

        this.srcUUID = srcUUID;
        this.srcObjectType = srcObjectType;
        this.SrcIndexInGlobalList = SrcIndexInGlobalList;

        this.cordX = cordX;
        this.cordY = cordY;

        this.velX = velX;
        this.velY = velY;

        this.damage = damage;
        this.hpCurrent = hpCurrent;

        this.myType = myType;
        this.objectName = objectName;

        myCPolygon = new CPolygon();

        switch (myType) {
            case PLAYER -> { myCPolygon.InitPreMade(PreMadeType.PENTAGON,CONFIG.SIZE_PLAYER); MyColor = Color.GREEN; }
            case ENEMY -> { myCPolygon.InitPreMade(PreMadeType.SQUARE,CONFIG.SIZE_ENEMY); MyColor = Color.ORANGE; }
            case BULLET_LARGE -> { myCPolygon.InitPreMade(PreMadeType.TRIANGLE,CONFIG.SIZE_LARGE_BULLET); MyColor = Color.RED; }
            case BULLET_NORMAL -> { myCPolygon.InitPreMade(PreMadeType.TRIANGLE,CONFIG.SIZE_NORMAL_BULLET); MyColor = Color.RED; }
            case BULLET_SMALLER -> { myCPolygon.InitPreMade(PreMadeType.TRIANGLE,CONFIG.SIZE_SMALL_BULLET); MyColor = Color.RED; }
            case TRAP -> { myCPolygon.InitPreMade(PreMadeType.SQUARE,CONFIG.SIZE_TRAP); MyColor = Color.PINK; }
            case BOOSTER -> { myCPolygon.InitPreMade(PreMadeType.SQUARE,CONFIG.SIZE_BOOSTER); MyColor = Color.WHITE; }
        }

        switch (new Random().nextInt(4)) {
            case 0 -> myWeapon = WeaponType.SHOTGUN;
            case 1 -> myWeapon = WeaponType.SNIPER_RIFLE;
            case 2 -> myWeapon = WeaponType.PISTOL;
            case 3 -> myWeapon = WeaponType.RIFLE;
        }

        if (myType == ObjectType.BULLET_LARGE || myType == ObjectType.BULLET_NORMAL || myType == ObjectType.BULLET_SMALLER)
            myWeapon = null;

    }

    public void Move(){

        cordY += velY;
        cordX += velX;

        myCPolygon.UpdateOffsets(cordX, cordY);

        if (CONFIG.ANIMATION_ROTATION)
            myCPolygon.Rotate(true, rotation);

        if (myType == ObjectType.PLAYER || myType == ObjectType.ENEMY) {
            if (cordX > (CONFIG.WINDOW_WIDTH + 50)) {
                cordX = 0;
            } else if (cordX < -50) {
                cordX = CONFIG.WINDOW_WIDTH;
            }
            if (cordY > CONFIG.WINDOW_HEIGHT) {
                cordY = -50;

            }
        }

    }
    public void ReplaceMe(GameObject gameObject){
        this.visible = gameObject.visible;
        this.myCPolygon = gameObject.myCPolygon;
        this.MyColor = gameObject.MyColor;
        this.MyScore = gameObject.MyScore;
        this.MyIndexInGlobalList = gameObject.MyIndexInGlobalList;
        this.SrcIndexInGlobalList = gameObject.SrcIndexInGlobalList;

        this.statKillCount = gameObject.statKillCount;
        this.statTrapCount = gameObject.statTrapCount;
        this.statBoosterCount = gameObject.statBoosterCount;
        this.statDamageDealt = gameObject.statDamageDealt;
        this.statDamageTaken = gameObject.statDamageTaken;
        this.statShootCount = gameObject.statShootCount;

        this.cordX = gameObject.cordX;
        this.cordY = gameObject.cordY;
        this.velY = gameObject.velY;
        this.velX = gameObject.velX;

        this.damage = gameObject.damage;
        this.hpCurrent = gameObject.hpCurrent;
        this.reloadTime = gameObject.reloadTime;
        this.myType = gameObject.myType;
        this.rotation = gameObject.rotation;
        this.wallet = gameObject.wallet;
        this.objectName = gameObject.objectName;
        this.myWeapon = gameObject.myWeapon;
    }
    public void Shoot(double targetCordX, double targetCordY){

        double tempSourceX = cordX, tempSourceY = cordY;

        double distX = Math.abs(tempSourceX - targetCordX);
        double distY = Math.abs(tempSourceY - targetCordY);

        double proportions = CONFIG.RECOIL_POWER / (distX + distY);

        double speedX = distX * proportions;
        double speedY = distY * proportions;

        if (tempSourceX > targetCordX && speedX > 0)
            speedX *= -1;
        if (tempSourceX < targetCordX && speedX < 0)
            speedX *= -1;
        if (tempSourceY > targetCordY && speedY > 0)
            speedY *= -1;
        if (tempSourceY < targetCordY && speedY < 0)
            speedY *= -1;

        if (reloadTime <= 0) {
            List<GameObject> Bullets = new ArrayList<>();
            statShootCount++;
            if (myType == ObjectType.PLAYER)
                Sound.PlayOofSound();

            if (myWeapon == WeaponType.SHOTGUN) {
                Bullets.add(new GameObject(cordX, cordY, damage * CONFIG.SHOTGUN_PRIME_BULLET, 100.0, ObjectType.BULLET_LARGE, "Bullet",speedX * 1.3, speedY, myUUID, myType, MyIndexInGlobalList));
                Bullets.add(new GameObject(cordX, cordY * 0.9, damage * CONFIG.SHOTGUN_SECONDARY_BULLET, 100.0, ObjectType.BULLET_NORMAL, "Bullet",speedX * 1.2, speedY, myUUID, myType, MyIndexInGlobalList));
                Bullets.add(new GameObject(cordX, cordY * 1.1, damage * CONFIG.SHOTGUN_SECONDARY_BULLET, 100.0, ObjectType.BULLET_NORMAL, "Bullet",speedX * 1.2, speedY, myUUID, myType, MyIndexInGlobalList));
                Bullets.add(new GameObject(cordX, cordY * 0.8, damage * CONFIG.SHOTGUN_TERTIARY_BULLET, 100.0, ObjectType.BULLET_SMALLER, "Bullet",speedX * 1.1, speedY, myUUID, myType, MyIndexInGlobalList));
                Bullets.add(new GameObject(cordX, cordY * 1.2, damage * CONFIG.SHOTGUN_TERTIARY_BULLET, 100.0, ObjectType.BULLET_SMALLER, "Bullet",speedX * 1.1, speedY, myUUID, myType, MyIndexInGlobalList));
                velX = (-1.3 * speedX);
                velY = (-1.3 * speedY);
                reloadTime = CONFIG.SHOTGUN_RELOAD;
            }
            else if (myWeapon == WeaponType.PISTOL) {
                Bullets.add(new GameObject(cordX, cordY, damage * CONFIG.PISTOL_BULLET, 100.0, ObjectType.BULLET_NORMAL, "Bullet",speedX, speedY, myUUID, myType, MyIndexInGlobalList));
                velX = (-1 * speedX);
                velY = (-1 * speedY);
                reloadTime = CONFIG.PISTOL_RELOAD;
            }
            else if (myWeapon == WeaponType.RIFLE) {
                Bullets.add(new GameObject(cordX, cordY, damage * CONFIG.RIFLE_BULLET, 100.0, ObjectType.BULLET_SMALLER, "Bullet",speedX * 1.1, speedY, myUUID, myType, MyIndexInGlobalList));
                Bullets.add(new GameObject(cordX, cordY, damage * CONFIG.RIFLE_BULLET, 100.0, ObjectType.BULLET_SMALLER, "Bullet",speedX * 1.3, speedY, myUUID, myType, MyIndexInGlobalList));
                Bullets.add(new GameObject(cordX, cordY, damage * CONFIG.RIFLE_BULLET, 100.0, ObjectType.BULLET_SMALLER, "Bullet",speedX * 1.5, speedY, myUUID, myType, MyIndexInGlobalList));
                velX = (-1.1 * speedX);
                velY = (-1.1 * speedY);
                reloadTime = CONFIG.RIFLE_RELOAD;
            }
            else if (myWeapon == WeaponType.SNIPER_RIFLE) {
                Bullets.add(new GameObject(cordX, cordY, damage * CONFIG.SNIPER_BULLET, 100.0, ObjectType.BULLET_LARGE, "Bullet",speedX * 3, speedY, myUUID,myType, MyIndexInGlobalList));
                velX = (-1.3 * speedX);
                velY = (-1.3 * speedY);
                reloadTime = CONFIG.SNIPER_RELOAD;
            }

            if (myType == ObjectType.PLAYER) {
                Main.PacketToSend.PlayerList.add(this);
                Main.BulletList.addAll(Bullets);
                Main.PacketToSend.BulletList.addAll(Bullets);
            } else if (myType == ObjectType.ENEMY) {
                HostModule.GlobalPackage.BulletList.addAll(Bullets);
                for (ServerModule serverModule : HostModule.threadList) {
                    serverModule.packetToSend.BulletList.addAll(Bullets);
                    serverModule.packetToSend.EnemyList.add(this);
                }
            }

        }
    }
    public boolean CanAfford(int price) {
        return wallet - price > 0;
    }
    public void Charge(int price) {
        if (CanAfford(price))
            wallet -= price;
    }
}