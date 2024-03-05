package org.example.Online_Modules;

import org.example.Custom_Objects.GameObject;
import org.example.Custom_Objects.Packet;
import org.example.Main_Package.Main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerModule {
    private boolean clientIdSent = false;
    final int clientID;
    final String clientName;
    final Socket socket;
    final ObjectInputStream objectInputStream;
    final ObjectOutputStream objectOutputStream;
    public Packet packetToSend;
    ServerModule(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, int clientID) {
        this.socket = socket;
        this.clientID = clientID;
        this.clientName = "Client_0" + clientID;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        packetToSend = new Packet();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Packet tempPacket = new Packet();
                Packet AckPacket;

                Packet toBeRemoved = new Packet();

                boolean Running = true;

                while (Running) {
                    try { // Wysyłanie
                        if (!clientIdSent) {
                            objectOutputStream.writeObject(clientID);
                            clientIdSent = true;
                        }
                        else {
                            objectOutputStream.reset();
                            objectOutputStream.flush();

                            tempPacket.PlayerList.addAll(packetToSend.PlayerList);
                            tempPacket.EnemyList.addAll(packetToSend.EnemyList);
                            tempPacket.BulletList.addAll(packetToSend.BulletList);
                            tempPacket.TrapList.addAll(packetToSend.TrapList);
                            tempPacket.BoosterList.addAll(packetToSend.BoosterList);

                            packetToSend.PlayerList.clear();
                            packetToSend.EnemyList.clear();
                            packetToSend.BulletList.clear();
                            packetToSend.TrapList.clear();
                            packetToSend.BoosterList.clear();

                            objectOutputStream.writeUnshared(tempPacket);
                            tempPacket.PlayerList.clear();
                            tempPacket.EnemyList.clear();
                            tempPacket.BulletList.clear();
                            tempPacket.TrapList.clear();
                            tempPacket.BoosterList.clear();
                        }
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    try { // Odbieranie
                        AckPacket = (Packet) objectInputStream.readObject();
                        for (ServerModule serverThread : HostModule.threadList) {
                            if (serverThread.clientID != clientID) {
                                serverThread.packetToSend.PlayerList.addAll(AckPacket.PlayerList);
                                serverThread.packetToSend.EnemyList.addAll(AckPacket.EnemyList);
                                serverThread.packetToSend.BulletList.addAll(AckPacket.BulletList);
                            }
                        }

                        if (AckPacket.BulletList.size() > 0) {
                            for (GameObject AckBullet : AckPacket.BulletList) {
                                if (AckBullet != null) {
                                    boolean missing = true;
                                    for (GameObject LocalBullet : HostModule.GlobalPackage.BulletList) {
                                        if (LocalBullet.myUUID.compareTo(AckBullet.myUUID) == 0) {
                                            if (AckBullet.hpCurrent > 0) {
                                                LocalBullet.ReplaceMe(AckBullet);
                                            }
                                            else {
                                                //HostModule.GlobalPackage.BulletList.remove(LocalBullet);
                                                toBeRemoved.BulletList.add(LocalBullet);
                                            }
                                            missing = false;
                                        }
                                    }
                                    if (missing) {
                                        HostModule.GlobalPackage.BulletList.add(AckBullet);
                                    }
                                }
                            }
                        } // Handle Bullet List

                        if (AckPacket.EnemyList.size() > 0) {
                            for (GameObject AckEnemy : AckPacket.EnemyList) {
                                if (AckEnemy != null) {
                                    boolean missing = true;
                                    for (GameObject LocalEnemy : HostModule.GlobalPackage.EnemyList) {
                                        if (LocalEnemy.myUUID.compareTo(AckEnemy.myUUID) == 0) {
                                            if (AckEnemy.hpCurrent > 0) {
                                                LocalEnemy.ReplaceMe(AckEnemy);
                                            }
                                            else {
                                                //HostModule.GlobalPackage.BulletList.remove(LocalBullet);
                                                toBeRemoved.EnemyList.add(LocalEnemy);
                                            }
                                            missing = false;
                                        }
                                    }
                                    if (missing) {
                                        HostModule.GlobalPackage.EnemyList.add(AckEnemy);
                                    }
                                }
                            }
                        } // Handle Enemy List

                        if (AckPacket.PlayerList.size() > 0) {
                            for (GameObject AckPlayer : AckPacket.PlayerList) {
                                if (AckPlayer != null) {
                                    boolean missing = true;
                                    for (GameObject LocalPlayer : HostModule.GlobalPackage.PlayerList) {
                                        if (LocalPlayer.myUUID.compareTo(AckPlayer.myUUID) == 0) {
                                            if (AckPlayer.hpCurrent > 0) {
                                                LocalPlayer.ReplaceMe(AckPlayer);
                                            } else {
                                                //HostModule.GlobalPackage.PlayerList.remove(LocalPlayer);
                                                toBeRemoved.PlayerList.add(LocalPlayer);
                                            }
                                            missing = false;
                                        }
                                    }
                                    if (missing) {
                                        int x = HostModule.GlobalPackage.PlayerList.size();
                                        AckPlayer.MyIndexInGlobalList =  x;
                                        HostModule.GlobalPackage.PlayerList.add(AckPlayer);
                                        packetToSend.PlayerList.add(HostModule.GlobalPackage.PlayerList.get(x));
                                    }
                                }
                            }
                        }

                        if (AckPacket.TrapList.size() > 0) {
                            for (GameObject AckTrap : AckPacket.TrapList) {
                                if (AckTrap != null) {
                                    boolean missing = true;
                                    for (GameObject LocalTrap : HostModule.GlobalPackage.TrapList) {
                                        if (LocalTrap.myUUID.compareTo(AckTrap.myUUID) == 0) {
                                            if (AckTrap.hpCurrent > 0) {
                                                LocalTrap.ReplaceMe(AckTrap);
                                            }
                                            else {
                                                //HostModule.GlobalPackage.BulletList.remove(LocalBullet);
                                                toBeRemoved.TrapList.add(LocalTrap);
                                            }
                                            missing = false;
                                        }
                                    }
                                    if (missing) {
                                        HostModule.GlobalPackage.TrapList.add(AckTrap);
                                    }
                                }
                            }
                        } // Handle Trap List

                        if (AckPacket.BoosterList.size() > 0) {
                            for (GameObject AckBooster : AckPacket.BoosterList) {
                                if (AckBooster != null) {
                                    boolean missing = true;
                                    for (GameObject LocalBooster : HostModule.GlobalPackage.BoosterList) {
                                        if (LocalBooster.myUUID.compareTo(AckBooster.myUUID) == 0) {
                                            if (AckBooster.hpCurrent > 0) {
                                                LocalBooster.ReplaceMe(AckBooster);
                                            }
                                            else {
                                                //HostModule.GlobalPackage.BulletList.remove(LocalBullet);
                                                toBeRemoved.BoosterList.add(LocalBooster);
                                            }
                                            missing = false;
                                        }
                                    }
                                    if (missing) {
                                        HostModule.GlobalPackage.BoosterList.add(AckBooster);
                                    }
                                }
                            }
                        } // Handle Trap List

                        AckPacket.ClearPacket();

                        HostModule.GlobalPackage.BulletList.removeAll(toBeRemoved.BulletList);
                        HostModule.GlobalPackage.EnemyList.removeAll(toBeRemoved.EnemyList);
                        HostModule.GlobalPackage.PlayerList.removeAll(toBeRemoved.PlayerList);
                        HostModule.GlobalPackage.BoosterList.removeAll(toBeRemoved.BoosterList);
                        HostModule.GlobalPackage.TrapList.removeAll(toBeRemoved.TrapList);

                        toBeRemoved.BulletList.clear();
                        toBeRemoved.EnemyList.clear();
                        toBeRemoved.PlayerList.clear();
                        toBeRemoved.BoosterList.clear();
                        toBeRemoved.TrapList.clear();

                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        Running = false;
                    }
                }
            }
        }).start();

    }

}
