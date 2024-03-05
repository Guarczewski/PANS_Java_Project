package org.example.Config;

import java.util.UUID;

public class CONFIG {
    // GENERAL CONFIG
    public static int BACKGROUND_VOLUME = 50;
    public static int SHOOTING_VOLUME = 50;
    public static boolean ANIMATION_ROTATION = true;
    public static boolean SIMPLE_SHAPES = false;
    public static final UUID CLIENT_UUID = UUID.randomUUID();
    public static final int WINDOW_WIDTH = 1920; // WINDOW WIDTH
    public static final int WINDOW_HEIGHT = 1080; // WINDOW HEIGHT
    public static final int PORT_WINDOW_WIDTH = 500; // WINDOW WIDTH
    public static final int PORT_WINDOW_HEIGHT = 750; // WINDOW HEIGHT
    public static final int SCOREBOARD_WINDOW_WIDTH = 500; // WINDOW WIDTH
    public static final int SCOREBOARD_WINDOW_HEIGHT = 750; // WINDOW HEIGHT
    public static final int QUIZ_WINDOW_HEIGHT = 360;
    public static final int QUIZ_WINDOW_WIDTH = 640;
    public static final double RECOIL_POWER = 5; // MAX VALUE OF REVERSE VELOCITY AFTER SHOOT
    public static final double AMOUNT_OF_TICKS = 60.0; // MAX TICKS PER SECOND
    public static final double AMOUNT_OF_FRAMES = 120.0; // MAX FRAMES PER SECOND
    public static int PORT = 7777; // MULTIPLAYER PORT
    public static String SERVER_IP = "localhost";
    public static String CLIENT_NAME = "Offline"; // CLIENT DISPLAY NAME
    public static int PORT_SEARCH_MAX = 7500;
    public static int PORT_SEARCH_MIN = 7450;
    // BOOSTER SETTINGS
    public static final int BOOSTER_HP_BUFF = 25;
    public static final int BOOSTER_DAMAGE_BUFF = 5;
    // OBJECT DRAWING
    public static final int SIZE_PLAYER = 5;
    public static final int SIZE_ENEMY = 4;
    public static final int SIZE_LARGE_BULLET = 3;
    public static final int SIZE_NORMAL_BULLET = 2;
    public static final int SIZE_SMALL_BULLET= 1;
    public static final int SIZE_BOOSTER = 4;
    public static final int SIZE_TRAP = 4;
    // WEAPONS
    public static final int SHOTGUN_RELOAD = 1;
    public static final int SNIPER_RELOAD = 1;
    public static final int PISTOL_RELOAD = 1;
    public static final int RIFLE_RELOAD = 1;
    // SHOP PRICES
    // 1. SHOTGUN || 2. SNIPER || 3. PISTOL || 4.RIFLE //
    // 5. HEAL || 6. DAMAGE //
    public static final int[] SHOP_PRICES = {100,100,100,100,100,100};
    // SHOP BOOSTS
    public static final double HEAL_AMOUNT = 100;
    public static final double DAMAGE_BOOST_AMOUNT = 100;
    // Damage
    public static final double SHOTGUN_PRIME_BULLET = 1.25;
    public static final double SHOTGUN_SECONDARY_BULLET = 0.75;
    public static final double SHOTGUN_TERTIARY_BULLET = 0.25;
    public static final double SNIPER_BULLET = 3.75;
    public static final double PISTOL_BULLET = 0.75;
    public static final double RIFLE_BULLET = 0.50;

}
