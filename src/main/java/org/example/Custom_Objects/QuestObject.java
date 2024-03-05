package org.example.Custom_Objects;

import org.example.Main_Package.Main;

import java.util.Random;

public class QuestObject {
    public int targetStatKillCount, targetStatTrapCount, targetStatBoosterCount, targetStatDamageTaken, targetStatDamageDealt, targetStatShootCount;
    public int rewardCash = 100, rewardHeal = 100;

    public QuestObject(int targetStatKillCount, int targetStatTrapCount, int targetStatBoosterCount, int targetStatDamageTaken, int targetStatDamageDealt, int targetStatShootCount) {
        this.targetStatKillCount = targetStatKillCount;
        this.targetStatTrapCount = targetStatTrapCount;
        this.targetStatBoosterCount = targetStatBoosterCount;
        this.targetStatDamageTaken = targetStatDamageTaken;
        this.targetStatDamageDealt = targetStatDamageDealt;
        this.targetStatShootCount = targetStatShootCount;
    }

    public void NewQuest(){

        this.targetStatKillCount = 0;
        this.targetStatTrapCount = 0;
        this.targetStatBoosterCount = 0;
        this.targetStatDamageTaken = 0;
        this.targetStatDamageDealt = 0;
        this.targetStatShootCount = 0;

        Random random = new Random();

        for (int i = 0; i < (random.nextInt(5) + 1); i++) {
            boolean failure = true;
            do {
                switch (random.nextInt(6)) {
                    case 0 -> {
                        if (targetStatKillCount == 0) {
                            targetStatKillCount = (int) (3 + (Main.PlayerList.get(Main.MyPlayerIndex).statKillCount) * 1.2);
                            failure = false;
                        }
                    }
                    case 1 -> {
                        if (targetStatTrapCount == 0) {
                            targetStatTrapCount = (int) (3 + (Main.PlayerList.get(Main.MyPlayerIndex).statTrapCount) * 1.2);
                            failure = false;
                        }
                    }
                    case 2 -> {
                        if (targetStatBoosterCount == 0) {
                            targetStatBoosterCount = (int) (3 + (Main.PlayerList.get(Main.MyPlayerIndex).statBoosterCount) * 1.2);
                            failure = false;
                        }
                    }
                    case 3 -> {
                        if (targetStatDamageTaken == 0) {
                            targetStatDamageTaken = (int) (3 + (Main.PlayerList.get(Main.MyPlayerIndex).statDamageTaken) * 1.2);
                            failure = false;
                        }
                    }
                    case 4 -> {
                        if (targetStatDamageDealt == 0) {
                            targetStatDamageDealt = (int) (3 + (Main.PlayerList.get(Main.MyPlayerIndex).statDamageDealt) * 1.2);
                            failure = false;
                        }
                    }
                    case 5 -> {
                        if (targetStatShootCount == 0) {
                            targetStatShootCount = (int) (3 + (Main.PlayerList.get(Main.MyPlayerIndex).statShootCount) * 1.2);
                            failure = false;
                        }
                    }
                }
            }
            while (failure);
        }
    }

    public void QuestTracker() {
        if (Main.PlayerList.contains(Main.playerHolder)) {
            boolean Success = true;

            if (Main.PlayerList.get(Main.MyPlayerIndex).statKillCount < targetStatKillCount)
                Success = false;
            else if (Main.PlayerList.get(Main.MyPlayerIndex).statTrapCount < targetStatTrapCount)
                Success = false;
            else if (Main.PlayerList.get(Main.MyPlayerIndex).statBoosterCount < targetStatBoosterCount)
                Success = false;
            else if (Main.PlayerList.get(Main.MyPlayerIndex).statDamageTaken < targetStatDamageTaken)
                Success = false;
            else if (Main.PlayerList.get(Main.MyPlayerIndex).statDamageDealt < targetStatDamageDealt)
                Success = false;
            else if (Main.PlayerList.get(Main.MyPlayerIndex).statShootCount < targetStatShootCount)
                Success = false;

            if (Success) {
                Main.PlayerList.get(Main.MyPlayerIndex).hpCurrent += rewardHeal;
                if (Main.PlayerList.get(Main.MyPlayerIndex).hpCurrent > 100)
                    Main.PlayerList.get(Main.MyPlayerIndex).hpCurrent = 100;
                Main.PlayerList.get(Main.MyPlayerIndex).wallet += rewardCash;
                Main.PacketToSend.PlayerList.add(Main.PlayerList.get(Main.MyPlayerIndex));
                Main.activeQuestObject.NewQuest();
            }

        }
    }

}
