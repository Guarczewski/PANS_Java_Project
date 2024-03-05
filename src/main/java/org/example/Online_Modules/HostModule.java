package org.example.Online_Modules;

import org.example.Config.CONFIG;
import org.example.Custom_Enums.ObjectType;
import org.example.Custom_Objects.GameObject;
import org.example.Custom_Objects.Packet;
import org.example.Custom_Objects.SavedObject;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class HostModule extends JFrame {
    public static List<ServerModule> threadList;
    private int clientCount = 0;
    private static int packSize = 1;
    private static final UUID myUUID = UUID.randomUUID();
    public static Packet GlobalPackage;
    public static Random randomizer = new Random();
    public HostModule() {

        super("Sample");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,400,900);
        setVisible(true);
        setResizable(false);
        GlobalPackage = new Packet();

        threadList = new ArrayList<>();

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(CONFIG.PORT);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        ServerSocket finalServerSocket = serverSocket;

        StartUpdating();

        new Thread(() -> {
            while (true) {
                Socket socket;
                try {

                    System.out.println("Serwer czeka na klienta...");
                    socket = finalServerSocket.accept();
                    System.out.println("Serwer widzi nowego klienta");
                    clientCount++;

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    ServerModule serverModule = new ServerModule(socket, ois, oos, clientCount);
                    threadList.add(serverModule);

                    serverModule.packetToSend.BulletList.addAll(GlobalPackage.BulletList);
                    serverModule.packetToSend.PlayerList.addAll(GlobalPackage.PlayerList);
                    serverModule.packetToSend.EnemyList.addAll(GlobalPackage.EnemyList);
                    serverModule.packetToSend.TrapList.addAll(GlobalPackage.TrapList);
                    serverModule.packetToSend.BoosterList.addAll(GlobalPackage.BoosterList);

                    setTitle("Current Client Count: " + clientCount);

                    Spawning();

                } catch (Exception exception) {
                    exception.printStackTrace();
                    System.out.println("HM_001");
                }

            }
        }).start();

    }
    // Gameplay Section
    private static void StartUpdating(){
        new Thread(() -> {
            // FPS Stuff
            long lastTime = System.nanoTime();
            double nsUpdate = 1000000000.0 / CONFIG.AMOUNT_OF_TICKS;
            double deltaUpdate = 0;

            long timer = System.currentTimeMillis();

            while(true) {
                long now = System.nanoTime();

                deltaUpdate += (now - lastTime) / nsUpdate;
                lastTime = now;

                if (deltaUpdate >= 1) {
                    deltaUpdate--;
                    Update(); // Call Update Function to update positions
                    Collision(); // Call Collision Function to check for collisions
                }

                if(System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                }
            }
        }).start();
    } // Start Updating Thread
    public static void Update() {

        List<GameObject> temp = new ArrayList<>();
        temp.addAll(GlobalPackage.EnemyList);
        temp.addAll(GlobalPackage.BulletList);
        temp.addAll(GlobalPackage.PlayerList);
        temp.addAll(GlobalPackage.BoosterList);
        temp.addAll(GlobalPackage.TrapList);

        Point WindowCenter = new Point(CONFIG.WINDOW_WIDTH/2, CONFIG.WINDOW_HEIGHT/2);
        Point Temp = new Point(0,0);

        for (GameObject gameObject : temp) {

            gameObject.Move();

            if (gameObject.myType == ObjectType.ENEMY || gameObject.myType == ObjectType.PLAYER) {
                gameObject.reloadTime--;

                if (gameObject.velY < 5) {
                    gameObject.velY += 0.2;
                }
            }

            try {
                Temp.x = (int) gameObject.cordX;
                Temp.y = (int) gameObject.cordY;
                if (WindowCenter.distance(Temp) > 3000) {
                    switch (gameObject.myType) {
                        case ENEMY -> GlobalPackage.EnemyList.remove(gameObject);
                        case TRAP -> GlobalPackage.TrapList.remove(gameObject);
                        case BOOSTER -> GlobalPackage.BoosterList.remove(gameObject);
                        case BULLET_LARGE, BULLET_SMALLER, BULLET_NORMAL -> GlobalPackage.BulletList.remove(gameObject);
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    } // Optimisation Function
    public static void Collision(){

        try {

            List<GameObject> threatList = new ArrayList<>();
            List<GameObject> targetList = new ArrayList<>();

            threatList.addAll(GlobalPackage.BulletList);
            threatList.addAll(GlobalPackage.BoosterList);
            threatList.addAll(GlobalPackage.TrapList);

            targetList.addAll(GlobalPackage.PlayerList);
            targetList.addAll(GlobalPackage.EnemyList);

            boolean collision;

            if (threatList.size() > 0) {

                for (GameObject threat : threatList) {
                    for (GameObject target : targetList) {

                        collision = false;

                        if (threat.srcUUID.compareTo(target.myUUID) != 0)  // No Self Harm
                            if (threat.myCPolygon.GetPoly().intersects(target.myCPolygon.GetPoly().getBounds2D())) // Check For Collision
                                collision = true;

                        if (collision) {

                            if (target.myType == ObjectType.PLAYER && threat.srcObjectType == ObjectType.PLAYER)
                                continue;

                            if (target.myType == ObjectType.ENEMY && threat.srcObjectType == ObjectType.ENEMY)
                                continue;

                            if (target.myType == ObjectType.ENEMY && threat.myType == ObjectType.TRAP)
                                continue;

                            threat.hpCurrent = -1;
                            target.hpCurrent -= threat.damage;
                            target.statDamageTaken += threat.damage;



                            if (threat.myType == ObjectType.BOOSTER) {
                                target.damage += CONFIG.BOOSTER_DAMAGE_BUFF;
                                target.hpCurrent += CONFIG.BOOSTER_HP_BUFF;

                                if (target.hpCurrent > 100)
                                    target.hpCurrent = 100;
                            }

                            boolean additionalPlayerPackage = false;

                            if (threat.srcObjectType == ObjectType.PLAYER) {
                                try {
                                    GlobalPackage.PlayerList.get(threat.SrcIndexInGlobalList).statDamageDealt += threat.damage;
                                    additionalPlayerPackage = true;
                                }
                                catch (Exception ignored){
                                    // Threat source might not be in game anymore
                                }
                            }

                            if (target.hpCurrent <= 0) {
                                switch (target.myType) {
                                    case ENEMY -> {
                                        GlobalPackage.EnemyList.remove(target);
                                        if (threat.srcObjectType == ObjectType.PLAYER) {
                                            if (GlobalPackage.PlayerList.size() > 0) {
                                                try {
                                                    GlobalPackage.PlayerList.get(threat.SrcIndexInGlobalList).statKillCount++;
                                                    additionalPlayerPackage = true;
                                                }
                                                catch (Exception ignored) {
                                                    // Threat source might not be in game anymore
                                                }
                                            }
                                        }
                                    }
                                    case PLAYER -> {
                                        GlobalPackage.PlayerList.remove(target);
                                        SavedObject.Save(target.objectName, target.MyScore);
                                    }
                                }
                            }

                            for (ServerModule serverModule : threadList) {

                                if (additionalPlayerPackage)
                                    serverModule.packetToSend.PlayerList.add(GlobalPackage.PlayerList.get(threat.SrcIndexInGlobalList));

                                switch (threat.myType) {
                                    case BOOSTER -> {
                                        serverModule.packetToSend.BoosterList.add(threat);
                                        target.statBoosterCount++;
                                    }
                                    case TRAP -> {
                                        serverModule.packetToSend.TrapList.add(threat);
                                        target.statTrapCount++;
                                    }
                                    default -> serverModule.packetToSend.BulletList.add(threat);
                                }

                                switch (target.myType) {
                                    case ENEMY -> serverModule.packetToSend.EnemyList.add(target);
                                    case PLAYER -> serverModule.packetToSend.PlayerList.add(target);
                                }

                            }

                            switch (threat.myType) {
                                case BOOSTER -> GlobalPackage.BoosterList.remove(threat);
                                case TRAP -> GlobalPackage.TrapList.remove(threat);
                                default -> GlobalPackage.BulletList.remove(threat);
                            }
                            if (GlobalPackage.EnemyList.size() == 0)
                                EnemySpawning();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    } // Collision Function
    // End of Gameplay Section

    public static void Spawning(){
        new Thread(() -> {
            while (true) {

                Random random = new Random();

                switch (random.nextInt(3)) {
                    case 0 -> EnemySpawning();
                    case 1 -> BoosterSpawning();
                    case 2 -> TrapSpawning();
                }

                if (random.nextInt(50) == 1) {
                    packSize++;
                }

                try {
                    Thread.sleep(5000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public static void EnemySpawning(){

        Random random = new Random();

        for (int i = 0; i < packSize; i++) {

            GameObject tempEnemy = new GameObject(random.nextInt(CONFIG.WINDOW_WIDTH - 100) + 50, random.nextInt(CONFIG.WINDOW_HEIGHT - 100) + 50, 10, 1, ObjectType.ENEMY, "Enemy",0, 0, myUUID, ObjectType.GAME,-1);

            for (ServerModule serverModule : threadList) {
                serverModule.packetToSend.EnemyList.add(tempEnemy);
            }

            GlobalPackage.EnemyList.add(tempEnemy);

            new Thread(() -> {
                while (GlobalPackage.EnemyList.contains(tempEnemy)) {
                    if (GlobalPackage.PlayerList.size() > 0) {
                        int i1 = GlobalPackage.EnemyList.indexOf(tempEnemy);
                        //System.out.println(GlobalPackage.EnemyList.get(i).StringPosition());
                        GlobalPackage.EnemyList.get(i1).Shoot(
                                GlobalPackage.PlayerList.get(randomizer.nextInt(GlobalPackage.PlayerList.size())).cordX,
                                GlobalPackage.PlayerList.get(randomizer.nextInt(GlobalPackage.PlayerList.size())).cordY);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static void BoosterSpawning(){
        Random random = new Random();

        for (int i = 0; i < packSize; i++) {

            GameObject tempBooster = new GameObject(random.nextInt(CONFIG.WINDOW_WIDTH), random.nextInt(CONFIG.WINDOW_HEIGHT), 0, 100, ObjectType.BOOSTER, "",0, 1, myUUID,ObjectType.GAME, -1);
            tempBooster.objectName = "BOOSTER";

            for (ServerModule serverModule : threadList) {
                serverModule.packetToSend.BoosterList.add(tempBooster);
            }

            GlobalPackage.BoosterList.add(tempBooster);

        }
    }
    public static void TrapSpawning(){

        Random random = new Random();

        for (int i = 0; i < packSize; i++) {

            GameObject tempTrap = new GameObject(random.nextInt(CONFIG.WINDOW_WIDTH), random.nextInt(CONFIG.WINDOW_HEIGHT), 50, 100, ObjectType.TRAP,"", 0, 0, myUUID,ObjectType.GAME, -1);
            tempTrap.objectName = "TRAP";

            for (ServerModule serverModule : threadList) {
                serverModule.packetToSend.TrapList.add(tempTrap);
            }

            GlobalPackage.TrapList.add(tempTrap);

        }

    }
    public static boolean Available(int PORT) {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.close();
            return true;
        }
        catch (Exception e) {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    System.out.println("HM_004");
                }
            }
        }

        return false;

    } // Check if port is Available

}
