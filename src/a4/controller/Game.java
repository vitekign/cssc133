/* Created by Victor Ignatenkov on 2/9/15.*/

package a4.controller;
import a4.app.commands.*;
import a4.app.utilities.Utilities;
import a4.model.GameWorld;
import a4.view.MapView;
import a4.view.ScoreView;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/*
 * Override the JButton to avoid issues with
 * hitting "SPACE" key.
 */

class ButtonSpaceKeyFocusAgnostic extends JButton{
    public ButtonSpaceKeyFocusAgnostic(String str){
        super(str);
        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");

        this.setBackground(new Color(43, 43, 43));
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setForeground(Color.WHITE);
    }
}

/*
 * Game - takes a role of a controller.
 */

public class Game extends JFrame implements KeyListener{

    private GameWorld gw;
    private ScoreView scoreView;
    private MapView mapView;

    public Game() {
        super("My Racing Game");

        this.addKeyListener(this);
        this.setFocusable(true);

        gw = new GameWorld();
        gw.initLayout();
        gw.startTimer();

        scoreView = new ScoreView();
        mapView = new MapView();

        setLayout(new BorderLayout());

        gw.addObserver(scoreView);
        gw.addObserver(mapView);

        gw.notifyObserver();


        //**********************************************
        //                 COMMANDS                   **
        //**********************************************

        /**
         * Create a set of commands which are responsible
         * for specific business logic. Each command is created
         * only once because it is a singleton in its nature.
         */

        QuitTheGame quitAction = QuitTheGame.getInstance();
        CollideWithNPC colWithNPCAction = CollideWithNPC.getInstance();
        PickUpFuelCan pickUpFuelCanAction = PickUpFuelCan.getInstance();

        PlayPause playPause = PlayPause.getInstance();
        playPause.setTarget(gw);

        DeleteObject deleteObjectAction = DeleteObject.getInstance();
        deleteObjectAction.setTarget(gw);
        deleteObjectAction.setEnabled(false);

        AddPylon addPylonAction = AddPylon.getInstance();
        addPylonAction.setTarget(gw);
        addPylonAction.setEnabled(false);

        AddFuelCan addFuelCanAction = AddFuelCan.getInstance();
        addFuelCanAction.setTarget(gw);
        addFuelCanAction.setEnabled(false);

        TurnOnOffBezier addTurnOnBezierAction = TurnOnOffBezier.getInstance();
        addTurnOnBezierAction.setTarget(gw);
        addTurnOnBezierAction.setEnabled(false);

        AddOilSlick addOilSlickAction = AddOilSlick.getInstance();
        addOilSlickAction.setTarget(gw);

        ChangeColors changeColorsAction = ChangeColors.getInstance();
        changeColorsAction.setTarget(gw);

        TurnOnSound turnOnSoundAction = TurnOnSound.getInstance();
        turnOnSoundAction.setTarget(gw);

        New newAction = New.getInstance();
        newAction.putValue(Action.NAME, "New");

        Save saveAction = Save.getInstance();
        saveAction.putValue(Action.NAME, "Save");

        ShowAbout showAboutAction = ShowAbout.getInstance();

        Accelerate accelerateAction = Accelerate.getInstance();
        accelerateAction.setTarget(gw);

        Brake brakeAction = Brake.getInstance();
        brakeAction.setTarget(gw);

        TurnLeft turnLeftAction = TurnLeft.getInstance();
        turnLeftAction.setTarget(gw);

         TurnRight turnRightAction = TurnRight.getInstance();
        turnRightAction.setTarget(gw);

        SwitchStrategies switchStrategiesAction = SwitchStrategies.getInstance();
        switchStrategiesAction.setTarget(gw);

        RotateMuzzleToLeft rotateMuzzleToLeft = RotateMuzzleToLeft.getInstance();
        rotateMuzzleToLeft.setTarget(gw);

        RotateMuzzleToRight rotateMuzzleToRight = RotateMuzzleToRight.getInstance();
        rotateMuzzleToRight.setTarget(gw);

        LaunchMissile launchMissileAction = LaunchMissile.getInstance();
        launchMissileAction.setTarget(gw);
        //**********************************************
        //              top panel                     **
        //**********************************************

        /**
         * The ScoreView is implemented in a
         * different class, and then just added to the
         * main JPanel.
         */
        add(scoreView, BorderLayout.NORTH);


        //**********************************************
        //              left panel                    **
        //**********************************************

        /**
         * Left Panel - represents a set of buttons
         * which are located at the left side.
         * 1. Create a button.
         * 2. Supply the button's setAction with appropriate Command
         */

        JPanel leftPanel = new JPanel(){
//            @Override
//            protected void paintComponent(Graphics g) {
//                Image backgroundImage = Utilities.loadImage("motionBlur.png");
//                int tileSze = 150;
//                for(int i = 0; i < 15; i++){
//                    for(int j = 0; j < 4; j++){
//                        g.drawImage(backgroundImage,j*tileSze,i*tileSze,tileSze,tileSze,null);
//                    }
//                }
//
//            }
        };
        leftPanel.setBackground(new Color(22, 22, 22));

        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title;
        title = BorderFactory.createTitledBorder(
                loweredetched, "options");
        title.setTitleFont(new Font(Utilities.BASE_FONT, Font.PLAIN, 15));
        title.setTitleJustification(TitledBorder.CENTER);

        leftPanel.setBorder(title);

       ((TitledBorder)leftPanel.getBorder()).setTitleColor(new Color(101, 101, 101));


        leftPanel.setLayout(new GridLayout(20, 1));
        ((GridLayout)leftPanel.getLayout()).setVgap(10);

        this.add(leftPanel, BorderLayout.WEST);

        JButton playBtn = new ButtonSpaceKeyFocusAgnostic("Play");
        playBtn.setFont(new Font(Utilities.BASE_FONT, Font.BOLD, 22));
        leftPanel.add(playBtn);
        colWithNPCAction.setTarget(gw);
        playBtn.setAction(playPause);
        colWithNPCAction.putValue(Action.NAME, "Play");


        pickUpFuelCanAction.setTarget(gw);
        pickUpFuelCanAction.putValue(Action.NAME, "Pick Up FuelCan");

        JButton deleteGameObjectBtn = new ButtonSpaceKeyFocusAgnostic("Delete");
        leftPanel.add(deleteGameObjectBtn);
        deleteGameObjectBtn.setAction(deleteObjectAction);
        deleteGameObjectBtn.setFont(new Font(Utilities.BASE_FONT, Font.BOLD, 17));


        JButton addPylonBtn = new ButtonSpaceKeyFocusAgnostic("Add Pylon");
        leftPanel.add(addPylonBtn);
        addPylonBtn.setAction(addPylonAction);
        addPylonBtn.setFont(new Font(Utilities.BASE_FONT, Font.BOLD, 17));

        JButton addFuelCanBtn = new ButtonSpaceKeyFocusAgnostic("Add FuelCan");
        leftPanel.add(addFuelCanBtn);
        addFuelCanBtn.setAction(addFuelCanAction);
        addFuelCanBtn.setFont(new Font(Utilities.BASE_FONT, Font.BOLD, 17));

        JButton turnOnOffBezierCurveBtn = new ButtonSpaceKeyFocusAgnostic("turn OnOffBezierCurve");
        leftPanel.add(turnOnOffBezierCurveBtn);
        turnOnOffBezierCurveBtn.setAction(addTurnOnBezierAction);
        turnOnOffBezierCurveBtn.setFont(new Font(Utilities.BASE_FONT, Font.BOLD, 17));

        ButtonSpaceKeyFocusAgnostic quitTheGame = new ButtonSpaceKeyFocusAgnostic("");
        quitTheGame.setAction(quitAction);
        leftPanel.add(quitTheGame);
        quitAction.putValue(Action.NAME, "Quit");
        quitTheGame.setFont(new Font(Utilities.BASE_FONT, Font.BOLD, 22 ));



        //**********************************************
        //      Set up logic for key strokes.         **
        //**********************************************

        //TODO Find out how to switch the focus to another JComponent

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("C"), "pickUpFuelCan");
        this.getRootPane().getActionMap().put("pickUpFuelCan", pickUpFuelCanAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("O"), "addOilSlick");
        this.getRootPane().getActionMap().put("addOilSlick", addOilSlickAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("N"), "changeColors");
        this.getRootPane().getActionMap().put("changeColors", changeColorsAction);

//        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0),"accelerateTheCar");
//        this.getRootPane().getActionMap().put("accelerateTheCar", accelerateAction);

//        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"),"showDownTheCar");
//        this.getRootPane().getActionMap().put("showDownTheCar", brakeAction);

//        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,
//                0, false),"turnLeft");
//        this.getRootPane().getActionMap().put("turnLeft", turnLeftAction);
//
//        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,
//                0, false),"turnRight");
//        this.getRootPane().getActionMap().put("turnRight", turnRightAction);



//        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"),"switchStr");
//        this.getRootPane().getActionMap().put("switchStr", switchStrategiesAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"),"launchMissile");
        this.getRootPane().getActionMap().put("launchMissile", launchMissileAction);




        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Q"),"quit");
        this.getRootPane().getActionMap().put("quit", quitAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0),"deleteGameObjectBtn");
        this.getRootPane().getActionMap().put("deleteGameObjectBtn", deleteObjectAction);


//        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F,
//                0, false), "muzzleToRight");
//        this.getRootPane().getActionMap().put("muzzleToRight", rotateMuzzleToRight);
//
//
//        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S,
//                0 , false),"muzzleToLeft");
//        this.getRootPane().getActionMap().put("muzzleToLeft", rotateMuzzleToLeft);



        //**********************************************
        //              central panel                **
        //**********************************************


        /**
         * Central panel is implemented in another class
         * and then just added to the main JPanel.
         */
        this.add(mapView, BorderLayout.CENTER);


        //**********************************************
        //                    menu                    **
        //**********************************************

        /**
         * Create a menu in the main JPanel.
         * All buttons, in the menu, do specific functionality
         * by receiving a corresponding Command.
         */

        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        fileMenu.add(newMenuItem);
        newMenuItem.setAction(newAction);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        saveMenuItem.setAction(saveAction);

        JCheckBoxMenuItem soundMenuItem = new JCheckBoxMenuItem("Sound");
        soundMenuItem.setState(gw.isSound());
        fileMenu.add(soundMenuItem);
        soundMenuItem.setAction(turnOnSoundAction);
        turnOnSoundAction.putValue(Action.NAME, "Sound");


        JMenuItem aboutMenuItem = new JMenuItem("About");
        fileMenu.add(aboutMenuItem);
        aboutMenuItem.setAction(showAboutAction);


        JMenuItem quitTheGameMenuItem = new JMenuItem("Quit");
        fileMenu.add(quitTheGameMenuItem);
        quitTheGameMenuItem.setAction(quitAction);

        JMenu commandMenu = new JMenu("Commands");
        JMenuItem pickUpFuelItem = new JMenuItem("fuel pickup");
        pickUpFuelItem.setAction(pickUpFuelCanAction);
        commandMenu.add(pickUpFuelItem);


        JMenuItem deleteGameObjectMenuItem = new JMenuItem();
        deleteGameObjectMenuItem.setAction(deleteObjectAction);
        commandMenu.add(deleteGameObjectMenuItem);

        JMenuItem addPylonMenuItem = new JMenuItem();
        addPylonMenuItem.setAction(addPylonAction);
        commandMenu.add(addPylonMenuItem);

        JMenuItem addFuelCanMenuItem = new JMenuItem();
        addFuelCanMenuItem.setAction(addFuelCanAction);
        commandMenu.add(addFuelCanMenuItem);

        JMenuItem addOilSlickItem = new JMenuItem("add oil slick");
        commandMenu.add(addOilSlickItem);
        addOilSlickItem.setAction(addOilSlickAction);
        addOilSlickAction.putValue(Action.NAME, "Add Oil Slick");

        JMenuItem generateNewColorsItem = new JMenuItem("new colors");
        commandMenu.add(generateNewColorsItem);
        generateNewColorsItem.setAction(changeColorsAction);
        changeColorsAction.putValue(Action.NAME, "Change Colors");

        bar.add(fileMenu);
        bar.add(commandMenu);
        this.setJMenuBar(bar);


        //**********************************************
        //              main JPanel                   **
        //**********************************************
        /**
         * Set up the main JPanel.
         */

        setResizable(true);
        setSize(gw.GLOBAL_WIDTH, gw.GLOBAL_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        /**
         * Right now play() is not used,
         * however, it might be needed in the next
         * versions of the game by implementing
         * a game loop.
         */
      //  play();

    }

    /**
     * Main loop of the game
     * In the current version, the game loop is not used.
     **/
    private void play() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
       if(e.getKeyCode() == KeyEvent.VK_S){
           gw.VK_S = true;
       } else if(e.getKeyCode() == KeyEvent.VK_F){
           gw.VK_F = true;
       } else if(e.getKeyCode() == KeyEvent.VK_LEFT){
           gw.VK_LEFT = true;
       } else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
           gw.VK_RIGHT = true;
       } else if(e.getKeyCode() == KeyEvent.VK_UP){
           gw.VK_UP = true;

       }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_S){
            gw.VK_S = false;
        } else if(e.getKeyCode() == KeyEvent.VK_F){
            gw.VK_F = false;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            gw.VK_LEFT = false;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            gw.VK_RIGHT = false;
        } else if(e.getKeyCode() == KeyEvent.VK_UP){
            gw.VK_UP = false;
        }
    }
}