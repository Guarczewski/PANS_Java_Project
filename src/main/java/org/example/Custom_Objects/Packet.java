package org.example.Custom_Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Packet implements Serializable {
    public List<GameObject> PlayerList = new ArrayList<>();
    public List<GameObject> EnemyList = new ArrayList<>();
    public List<GameObject> BulletList = new ArrayList<>();
    public List<GameObject> BoosterList = new ArrayList<>();
    public List<GameObject> TrapList = new ArrayList<>();
    public void ClearPacket() {
        this.PlayerList.clear();
        this.EnemyList.clear();
        this.BulletList.clear();
        this.TrapList.clear();
        this.BoosterList.clear();
    }
}
