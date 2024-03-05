package org.example.Online_Modules;

import org.example.Custom_Objects.GameObject;
import org.example.Config.CONFIG;
import org.example.Custom_Objects.Packet;
import org.example.Custom_Panels.CPlayerList;
import org.example.Custom_Panels.CScoreboardPanel;
import org.example.Main_Package.Main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientModule {
    public int ClientID = -1;
    public String ClientName;
    public ClientModule(){
        // Basic JFrame Thingy

        Socket socket;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            socket = new Socket("localhost",  CONFIG.PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e){
            System.out.println("Error during creating connection");
        }

        ObjectInputStream finalObjectInputStream = objectInputStream;
        ObjectOutputStream finalObjectOutputStream = objectOutputStream;

        new Thread(() -> {
            Packet tempPacket = new Packet();
            Packet AckPacket;

            Packet toBeRemoved = new Packet();

            while (true) {
                try {
                    if (ClientID == -1) {
                        int tmp = (int) finalObjectInputStream.readObject();
                        ClientName = "Client_0" + tmp;
                        ClientID = tmp;
                        CONFIG.CLIENT_NAME = ClientName;
                    }
                    else {

                        try {
                            AckPacket = (Packet) finalObjectInputStream.readObject();
                            if (AckPacket.BulletList.size() > 0) {
                                for (GameObject AckBullet : AckPacket.BulletList) {
                                    if (AckBullet != null) {
                                        boolean missing = true;
                                        for (GameObject LocalBullet : Main.BulletList) {
                                            if (LocalBullet.myUUID.compareTo(AckBullet.myUUID) == 0) {
                                                if (AckBullet.hpCurrent > 0) {
                                                    LocalBullet.ReplaceMe(AckBullet);
                                                }
                                                else {
                                                    toBeRemoved.BulletList.add(LocalBullet);
                                                   // Main.BulletList.remove(LocalBullet);
                                                }
                                                missing = false;
                                            }
                                        }
                                        if (missing) {
                                            Main.BulletList.add(AckBullet);
                                        }
                                    }
                                }
                            } // Handle Bullet List

                            if (AckPacket.EnemyList.size() > 0) {
                                for (GameObject AckEnemy : AckPacket.EnemyList) {
                                    if (AckEnemy != null) {
                                        boolean missing = true;
                                        for (GameObject LocalEnemy : Main.EnemyList) {
                                            if (LocalEnemy.myUUID.compareTo(AckEnemy.myUUID) == 0) {
                                                if (AckEnemy.hpCurrent > 0) {
                                                    LocalEnemy.ReplaceMe(AckEnemy);
                                                }
                                                else {
                                                    toBeRemoved.EnemyList.add(LocalEnemy);
                                                    // Main.EnemyList.remove(LocalEnemy);
                                                }
                                                missing = false;

                                            }
                                        }
                                        if (missing) {
                                            Main.EnemyList.add(AckEnemy);
                                        }
                                    }
                                }
                            } // Handle Enemy List

                            if (AckPacket.PlayerList.size() > 0) {
                                for (GameObject AckPlayer : AckPacket.PlayerList) {
                                    if (AckPlayer != null) {
                                        boolean missing = true;
                                        for (GameObject LocalPlayer : Main.PlayerList) {
                                            if (LocalPlayer.myUUID.compareTo(AckPlayer.myUUID) == 0) {
                                                if (AckPlayer.hpCurrent > 0) {
                                                    LocalPlayer.ReplaceMe(AckPlayer);
                                                }
                                                else {
                                                   // Main.PlayerList.remove(LocalPlayer);
                                                    toBeRemoved.PlayerList.add(LocalPlayer);
                                                }
                                                missing = false;
                                            }
                                        }
                                        if (missing) {
                                            Main.PlayerList.add(AckPlayer);
                                        }
                                    }
                                }
                            } // Handle Player List

                            if (AckPacket.TrapList.size() > 0) {
                                for (GameObject AckTrap : AckPacket.TrapList) {
                                    if (AckTrap != null) {
                                        boolean missing = true;
                                        for (GameObject localTrap : Main.TrapList) {
                                            if (localTrap.myUUID.compareTo(AckTrap.myUUID) == 0) {
                                                if (AckTrap.hpCurrent > 0) {
                                                    localTrap.ReplaceMe(AckTrap);
                                                }
                                                else {
                                                   // Main.PlayerList.remove(LocalPlayer);
                                                    toBeRemoved.TrapList.add(localTrap);
                                                }
                                                missing = false;
                                            }
                                        }
                                        if (missing) {
                                            Main.TrapList.add(AckTrap);
                                        }
                                    }
                                }
                            } // Handle Trap List

                            if (AckPacket.BoosterList.size() > 0) {
                                for (GameObject AckBooster : AckPacket.BoosterList) {
                                    if (AckBooster != null) {
                                        boolean missing = true;
                                        for (GameObject LocalBooster : Main.BoosterList) {
                                            if (LocalBooster.myUUID.compareTo(AckBooster.myUUID) == 0) {
                                                if (AckBooster.hpCurrent > 0) {
                                                    LocalBooster.ReplaceMe(AckBooster);
                                                }
                                                else {
                                                   // Main.PlayerList.remove(LocalPlayer);
                                                    toBeRemoved.BoosterList.add(LocalBooster);
                                                }
                                                missing = false;
                                            }
                                        }
                                        if (missing) {
                                            Main.BoosterList.add(AckBooster);
                                        }
                                    }
                                }
                            } // Handle Player List

                            if (AckPacket.PlayerList.size() > 0 || AckPacket.EnemyList.size() > 0 || AckPacket.BulletList.size() > 0)
                                Main.NEW_LIST_UPDATE = true;

                            if (AckPacket.PlayerList.size() > 0 || AckPacket.EnemyList.size() > 0) {
                                CScoreboardPanel.UpdateScoreboardList();
                                CPlayerList.UpdatePlayerList();
                            }

                            AckPacket.ClearPacket();

                            Main.BulletList.removeAll(toBeRemoved.BulletList);
                            Main.EnemyList.removeAll(toBeRemoved.EnemyList);
                            Main.PlayerList.removeAll(toBeRemoved.PlayerList);
                            Main.BoosterList.removeAll(toBeRemoved.BoosterList);
                            Main.TrapList.removeAll(toBeRemoved.TrapList);

                            toBeRemoved.BulletList.clear();
                            toBeRemoved.EnemyList.clear();
                            toBeRemoved.PlayerList.clear();
                            toBeRemoved.BoosterList.clear();
                            toBeRemoved.TrapList.clear();

                            try {
                                Main.PlayerList.get(Main.MyPlayerIndex);
                            }
                            catch (Exception exception){
                                if (!Main.GAME_INSTANCE.lastChanceQuiz.active) {
                                    Main.GAME_INSTANCE.lastChanceQuiz.active = true;
                                    Main.GAME_INSTANCE.lastChanceQuiz.setVisible(true);
                                }
                            }

                        }
                        catch (Exception ignored) {
                            return;
                        }

                    }

                } catch (Exception e) {
                    System.out.println("CM_001");
                    break;
                }

                try {
                    tempPacket.PlayerList.addAll(Main.PacketToSend.PlayerList);
                    tempPacket.BulletList.addAll(Main.PacketToSend.BulletList);

                    Main.PacketToSend.ClearPacket();

                    finalObjectOutputStream.reset();
                    finalObjectOutputStream.flush();
                    finalObjectOutputStream.writeUnshared(tempPacket);

                    tempPacket.ClearPacket();

                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
        }).start();
    }

}
