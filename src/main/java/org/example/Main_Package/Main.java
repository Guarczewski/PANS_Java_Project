package org.example.Main_Package;

import org.example.Custom_Elements.Sound;
import org.example.Custom_Enums.ObjectType;
import org.example.Custom_Enums.WeaponType;
import org.example.Custom_Frames.CPortListFrame;
import org.example.Custom_Frames.CScoreboardFrame;
import org.example.Custom_Objects.*;
import org.example.Custom_Panels.*;
import org.example.Config.*;
import org.example.Online_Modules.*;
import org.example.Custom_Frames.CQuizFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends JFrame implements ActionListener, MouseListener, DocumentListener, KeyListener, ChangeListener {
    public static QuestObject activeQuestObject;
    public static GameObject playerHolder;
    public CPanel cPanel;
    public CMenuPanel cMenuPanel;
    public CConnectionPanel cConnectionPanel;
    public CSettingsPanel cSettingsPanel;
    public CQuizFrame lastChanceQuiz;
    public static boolean displayPlayerList = false;
    public static boolean displayScoreboard = false;
    public static boolean displayShop = false;
    public static int MyPlayerIndex = 0;
    public static Main GAME_INSTANCE;
    private static ClientModule CLIENT_MODULE_INSTANCE;
    public static boolean NEW_LIST_UPDATE = true; // Tells if CustomPanels.CPanel needs to update its own lists
    public static boolean ONLINE_MODE = false, GAME_RUNNING = false;
    public static List<GameObject> PlayerList = new ArrayList<>(); // List containing all clients
    public static List<GameObject> BulletList = new ArrayList<>(); // List containing all bullets
    public static List<GameObject> EnemyList = new ArrayList<>();  // List containing all enemies
    public static List<GameObject> TrapList = new ArrayList<>();  // List containing all enemies
    public static List<GameObject> BoosterList = new ArrayList<>();  // List containing all enemies
    public static Packet PacketToSend = new Packet();

    Main(){
        super("Swing Shotgun Game!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0, CONFIG.WINDOW_WIDTH, CONFIG.WINDOW_HEIGHT);
        setResizable(false);
        Image backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("src/Package_Images/Background.png"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        cPanel = new CPanel(backgroundImage);
        cPanel.setLayout(new GridLayout(0,3));

        activeQuestObject = new QuestObject(30,30,20,30,30,30);

        cMenuPanel = new CMenuPanel();
        cConnectionPanel = new CConnectionPanel();
        cSettingsPanel = new CSettingsPanel();
        lastChanceQuiz = new CQuizFrame();
        this.addKeyListener(this);

        cPanel.addMouseListener(this); // Add Mouse Listener

        cSettingsPanel.AnimationButton.addActionListener(this); // Add Action Listener for Animation Button
        cSettingsPanel.SimplerShapesButton.addActionListener(this); // Add Action Listener for Shapes Button
        cSettingsPanel.backgroundVolumeSlider.addChangeListener(this);
        cSettingsPanel.shootingVolumeSlider.addChangeListener(this);

        cMenuPanel.StartGameButton.addActionListener(this); // Add Action Listener for Start Game Button
        cMenuPanel.OnlineConfigButton.addActionListener(this); // Add Action Listener for Online Config Button
        cMenuPanel.SettingsButton.addActionListener(this); // Add Action Listener for Settings Button
        cMenuPanel.FullScoreboardButton.addActionListener(this); // Add Action Listener for Settings Button

        cConnectionPanel.JoinServerButton.addActionListener(this); // Add Action Listener for Join Server Button
        cConnectionPanel.StartSearchButton.addActionListener(this); // Add Action Listener for Start Search Button
        cConnectionPanel.PortInput.getDocument().addDocumentListener(this);  // Add Document Listener for Port Input

        lastChanceQuiz.submitButton.addActionListener(this);

        cPanel.add(new JLabel());
        cPanel.add(cMenuPanel);
        cPanel.add(new JLabel());

        try {
            CShopPanel.shopkeeper = ImageIO.read(new File("src/Package_Images/Shopkeeper.png"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        CShopPanel.UpdateShopList();

        setContentPane(cPanel);
        setVisible(true);
    }
    public static void main(String[] args) {
        GAME_INSTANCE = new Main(); // Initialization of game window
    }
    public static void InitGame(){

        if (PlayerList.size() == 0)
            CreatePlayer();

        activeQuestObject.NewQuest();

        // Server Handling
        if (!ONLINE_MODE) {
            // Check For First Free PORT
            while (!HostModule.Available(CONFIG.PORT)){ // If Port is occupied
                CONFIG.PORT++; // Increase port
                if (CONFIG.PORT >= 65535) { // Check if port is greater or equals 65535 (the highest available port)
                    return;
                    // Return if there is no free ports ( Cancels game initialisation)
                    // Search starts from Config value which by default equals 7777
                }
            }
            ONLINE_MODE = true;
            new HostModule(); // Start Server
            CLIENT_MODULE_INSTANCE = new ClientModule(); // Start Client
        }
        GAME_RUNNING = true;

        if (CLIENT_MODULE_INSTANCE != null) {
            while (Objects.equals(CONFIG.CLIENT_NAME, "Offline"))
                continue;
        } // Waiting for getting new name

        try {
            Thread.sleep(1);
        }
        catch (Exception exception) {
            System.out.println("Sleeping Thread Error");
        }

        GAME_INSTANCE.remove(GAME_INSTANCE.cMenuPanel);
        GAME_INSTANCE.remove(GAME_INSTANCE.cConnectionPanel);
        GAME_INSTANCE.remove(GAME_INSTANCE.cSettingsPanel);
        Sound.PlayBackgroundSound();
        StartUpdating();

    }

    public static void Render(){
        GAME_INSTANCE.cPanel.repaint();
    }

    public static void CreatePlayer(){
        playerHolder = new GameObject(500, 500, 10, 100, ObjectType.PLAYER, GAME_INSTANCE.cMenuPanel.NickInput.getText(), 0,0,CONFIG.CLIENT_UUID, ObjectType.GAME, -1);
        PlayerList.add(playerHolder);
        PacketToSend.PlayerList.add(playerHolder);
        MyPlayerIndex = PlayerList.indexOf(playerHolder);
    }

    private static void StartUpdating(){
        new Thread(() -> {
            // FPS Stuff
            long lastTime = System.nanoTime();
            double nsRender = 1000000000.0 / CONFIG.AMOUNT_OF_FRAMES;
            double nsUpdate = 1000000000.0 / CONFIG.AMOUNT_OF_TICKS;
            double deltaRender = 0;
            double deltaUpdate = 0;
            int updates = 0;
            int frames = 0;
            long timer = System.currentTimeMillis();

            while(true) {
                long now = System.nanoTime();

                deltaRender += (now - lastTime) / nsRender;
                deltaUpdate += (now - lastTime) / nsUpdate;
                lastTime = now;

                if(deltaRender >= 1) {
                    deltaRender--;
                    Render();
                    frames++;
                }

                if (deltaUpdate >= 1) {
                    deltaUpdate--;
                    updates++;
                    Update();
                    Collision();
                }

                if(System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    GAME_INSTANCE.setTitle("PORT: " + CONFIG.PORT + " Ticks: " + updates + ", FPS: " + frames + ", " + CONFIG.CLIENT_NAME);
                    updates = 0;
                    frames = 0;
                }
            }
        }).start();
    }
    public static void Update() {

        List<GameObject> temp = new ArrayList<>();
        temp.addAll(EnemyList);
        temp.addAll(BulletList);
        temp.addAll(PlayerList);
        temp.addAll(BoosterList);
        temp.addAll(TrapList);

        Point WindowCenter = new Point(CONFIG.WINDOW_WIDTH/2, CONFIG.WINDOW_HEIGHT/2);
        Point Temp = new Point(0,0);

        for (GameObject gameObject : temp) {

            gameObject.Move();

            if (gameObject.myType == ObjectType.ENEMY || gameObject.myType == ObjectType.PLAYER) {
                gameObject.reloadTime--;

                if (gameObject.velY < 5)
                    gameObject.velY += 0.2;

            }

            try {
                Temp.x = (int) gameObject.cordX;
                Temp.y = (int) gameObject.cordY;
                if (WindowCenter.distance(Temp) > 3000) {
                    switch (gameObject.myType) {
                        case ENEMY -> EnemyList.remove(gameObject);
                        case TRAP -> TrapList.remove(gameObject);
                        case BOOSTER -> BoosterList.remove(gameObject);
                        case BULLET_LARGE, BULLET_SMALLER, BULLET_NORMAL -> BulletList.remove(gameObject);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        activeQuestObject.QuestTracker();

    }

    public static void Collision(){

        try {

            List<GameObject> threatList = new ArrayList<>();
            List<GameObject> targetList = new ArrayList<>();

            threatList.addAll(BulletList);
            threatList.addAll(BoosterList);
            threatList.addAll(TrapList);

            targetList.addAll(PlayerList);
            targetList.addAll(EnemyList);

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

                            if (target.hpCurrent - threat.damage < 0)
                                target.visible = false;

                            threat.visible = false;
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

    public static void LockMenus(){
        GAME_INSTANCE.cConnectionPanel.PortInput.setBackground(Color.ORANGE);
        GAME_INSTANCE.cConnectionPanel.PortInput.setEditable(false);
        GAME_INSTANCE.cConnectionPanel.AddressInput.setBackground(Color.ORANGE);
        GAME_INSTANCE.cConnectionPanel.AddressInput.setEditable(false);
        GAME_INSTANCE.cConnectionPanel.JoinServerButton.setBackground(Color.ORANGE);
        GAME_INSTANCE.cMenuPanel.StartGameButton.setBackground(Color.GREEN);
    }

    public static void MakePurchase(int Variant) {
        // 1 Shotgun, 2 Sniper, 3 Pistol, 4 Rifle, 5 Heal, 6 Damage
        switch (Variant) {
            case 0 -> {
                if (PlayerList.get(MyPlayerIndex).CanAfford(CONFIG.SHOP_PRICES[0])) {
                    PlayerList.get(MyPlayerIndex).Charge(CONFIG.SHOP_PRICES[0]);
                    PlayerList.get(MyPlayerIndex).myWeapon = WeaponType.SHOTGUN;
                    Main.PacketToSend.PlayerList.add(PlayerList.get(MyPlayerIndex));
                }
            }
            case 1 -> {
                if (PlayerList.get(MyPlayerIndex).CanAfford(CONFIG.SHOP_PRICES[1])) {
                    PlayerList.get(MyPlayerIndex).Charge(CONFIG.SHOP_PRICES[1]);
                    PlayerList.get(MyPlayerIndex).myWeapon = WeaponType.SNIPER_RIFLE;
                    Main.PacketToSend.PlayerList.add(PlayerList.get(MyPlayerIndex));
                }
            }
            case 2 -> {
                if (PlayerList.get(MyPlayerIndex).CanAfford(CONFIG.SHOP_PRICES[2])) {
                    PlayerList.get(MyPlayerIndex).Charge(CONFIG.SHOP_PRICES[2]);
                    PlayerList.get(MyPlayerIndex).myWeapon = WeaponType.PISTOL;
                    Main.PacketToSend.PlayerList.add(PlayerList.get(MyPlayerIndex));
                }
            }
            case 3 -> {
                if (PlayerList.get(MyPlayerIndex).CanAfford(CONFIG.SHOP_PRICES[3])) {
                    PlayerList.get(MyPlayerIndex).Charge(CONFIG.SHOP_PRICES[3]);
                    PlayerList.get(MyPlayerIndex).myWeapon = WeaponType.RIFLE;
                    Main.PacketToSend.PlayerList.add(PlayerList.get(MyPlayerIndex));
                }
            }
            case 4 -> {
                if (PlayerList.get(MyPlayerIndex).CanAfford(CONFIG.SHOP_PRICES[4])) {
                    PlayerList.get(MyPlayerIndex).Charge(CONFIG.SHOP_PRICES[4]);
                    PlayerList.get(MyPlayerIndex).hpCurrent += CONFIG.HEAL_AMOUNT;
                    if (PlayerList.get(MyPlayerIndex).hpCurrent > 100)
                        PlayerList.get(MyPlayerIndex).hpCurrent = 100;
                    Main.PacketToSend.PlayerList.add(PlayerList.get(0));
                }
            }
            case 5 -> {
                if (PlayerList.get(MyPlayerIndex).CanAfford(CONFIG.SHOP_PRICES[5])) {
                    PlayerList.get(MyPlayerIndex).Charge(CONFIG.SHOP_PRICES[5]);
                    PlayerList.get(MyPlayerIndex).damage += CONFIG.DAMAGE_BOOST_AMOUNT;
                    Main.PacketToSend.PlayerList.add(PlayerList.get(0));
                }
            }
        }
    }

    private void PortCheck() {
        try {
            int tempPort = Integer.parseInt(GAME_INSTANCE.cConnectionPanel.PortInput.getText());
            if (tempPort > 65535 || tempPort <= 0) {
                GAME_INSTANCE.cConnectionPanel.JoinServerButton.setBackground(Color.RED);
            }
            else {
                CONFIG.PORT = tempPort;
                if (HostModule.Available(CONFIG.PORT)) {
                    GAME_INSTANCE.cConnectionPanel.JoinServerButton.setBackground(Color.RED);
                }
                else {
                    GAME_INSTANCE.cConnectionPanel.JoinServerButton.setBackground(Color.GREEN);
                }
            }
        }
        catch (Exception ignored) {
            System.out.println("WRONG PORT INPUTTED");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == cSettingsPanel.AnimationButton) {
            CONFIG.ANIMATION_ROTATION = !CONFIG.ANIMATION_ROTATION;
            if (CONFIG.ANIMATION_ROTATION) {
                cSettingsPanel.AnimationButton.setText("Turn Off Animations");
                cSettingsPanel.AnimationButton.setBackground(Color.GREEN);
            }
            else {
                cSettingsPanel.AnimationButton.setText("Turn On Animations");
                cSettingsPanel.AnimationButton.setBackground(Color.RED);
            }
        }
        else if (source == cSettingsPanel.SimplerShapesButton) {
            CONFIG.SIMPLE_SHAPES = !CONFIG.SIMPLE_SHAPES;
            if (CONFIG.SIMPLE_SHAPES) {
                CONFIG.ANIMATION_ROTATION = false;
                cSettingsPanel.AnimationButton.setText("Turn On Animations");
                cSettingsPanel.AnimationButton.setBackground(Color.RED);

                cSettingsPanel.SimplerShapesButton.setText("Turn Off Simple Shapes");
                cSettingsPanel.SimplerShapesButton.setBackground(Color.GREEN);
            }
            else {
                CONFIG.ANIMATION_ROTATION = true;
                cSettingsPanel.AnimationButton.setText("Turn Off Animations");
                cSettingsPanel.AnimationButton.setBackground(Color.GREEN);

                cSettingsPanel.SimplerShapesButton.setText("Turn On Simple Shapes");
                cSettingsPanel.SimplerShapesButton.setBackground(Color.RED);
            }
        }
        else if (source == cMenuPanel.StartGameButton) {
            if (!GAME_RUNNING)
                InitGame();
        }
        else if (source == cMenuPanel.SettingsButton) {
            cPanel.removeAll();
            cSettingsPanel.visible = !cSettingsPanel.visible;

            if (cSettingsPanel.visible)
                cPanel.add(cSettingsPanel);
            else
                cPanel.add(new JLabel());

            cPanel.add(cMenuPanel);

            if (cConnectionPanel.visible)
                cPanel.add(cConnectionPanel);
            else
                cPanel.add(new JLabel());

            cPanel.revalidate();
            cPanel.repaint();
        }
        else if (source == cMenuPanel.OnlineConfigButton) {
            cPanel.removeAll();
            cConnectionPanel.visible = !cConnectionPanel.visible;

            if (cSettingsPanel.visible)
                cPanel.add(cSettingsPanel);
            else
                cPanel.add(new JLabel());

            cPanel.add(cMenuPanel);

            if (cConnectionPanel.visible)
                cPanel.add(cConnectionPanel);
            else
                cPanel.add(new JLabel());

            cPanel.revalidate();
            cPanel.repaint();
        }
        else if(source == cMenuPanel.FullScoreboardButton) {
            new CScoreboardFrame();
        }
        else if (source == cConnectionPanel.JoinServerButton) {
            if (!ONLINE_MODE) {
                if (!HostModule.Available(CONFIG.PORT)) {
                    CreatePlayer();
                    ONLINE_MODE = true;
                    CLIENT_MODULE_INSTANCE = new ClientModule();
                    LockMenus();
                }
                else {
                    System.out.println("THERE IS NO SERVER ON THIS PORT");
                }
            }
        }
        else if (source == cConnectionPanel.StartSearchButton) {
            try {
                new CPortListFrame(Integer.parseInt(cConnectionPanel.PortSearchStartInput.getText()), Integer.parseInt(cConnectionPanel.PortSearchEndInput.getText()));
            }
            catch (Exception ignored) {
                System.out.println("WRONG PORT INPUTTED");
            }
        }
        else if (source == lastChanceQuiz.submitButton) {

            String string;

            if (lastChanceQuiz.jRadioButtons[0].isSelected())
                string = lastChanceQuiz.jRadioButtons[0].getText();
            else if (lastChanceQuiz.jRadioButtons[1].isSelected())
                string = lastChanceQuiz.jRadioButtons[1].getText();
            else
                string = lastChanceQuiz.jRadioButtons[2].getText();

            if (CQuizFrame.selectedQuestion.CorrectAnswer.compareTo(string) == 0) {
                CreatePlayer();
                lastChanceQuiz.setVisible(false);
                lastChanceQuiz.active = false;
                lastChanceQuiz.SelectQuestion();
            }
            else {
                dispatchEvent(new WindowEvent(GAME_INSTANCE, WindowEvent.WINDOW_CLOSING));
            }

        }

        GAME_INSTANCE.requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (Main.PlayerList.size() > 0) {
            Main.PlayerList.get(MyPlayerIndex).Shoot(e.getX(), e.getY());
        }

        if (Main.displayShop) {
            for (int i = 0; i < CShopPanel.fakeButtons.size(); i++) {
                if (CShopPanel.fakeButtons.get(i).intersects(new Rectangle(e.getX(), e.getY(), 10, 10))) {
                    MakePurchase(i);
                }
            }
        }
        GAME_INSTANCE.requestFocus();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void insertUpdate(DocumentEvent e) {
        PortCheck();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        PortCheck();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        PortCheck();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P') {
            if (!displayPlayerList) {
                displayPlayerList = true;
            }
        }
        else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
            if (!displayScoreboard) {
                displayScoreboard = true;
            }
        }
        else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
            displayShop = !displayShop;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P') {
            if (displayPlayerList) {
                displayPlayerList = false;
            }
        }
        else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
            if (displayScoreboard) {
                displayScoreboard = false;
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == cSettingsPanel.backgroundVolumeSlider) {
            CONFIG.BACKGROUND_VOLUME = 100 - cSettingsPanel.backgroundVolumeSlider.getValue();
            if (CONFIG.BACKGROUND_VOLUME > 80)
                CONFIG.BACKGROUND_VOLUME = 80;
        }
        else if (e.getSource() == cSettingsPanel.shootingVolumeSlider) {
            CONFIG.SHOOTING_VOLUME = 100 - cSettingsPanel.shootingVolumeSlider.getValue();
            if (CONFIG.SHOOTING_VOLUME > 80)
                CONFIG.SHOOTING_VOLUME = 80;
        }
    }
}