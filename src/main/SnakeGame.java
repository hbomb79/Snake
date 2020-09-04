package main;

/*
 * Base class for this snake game; instantiates all core controllers, game engine, entity sprites and runs the main
 * render loop.
 *
 * Singleton class, fetch running instance using SnakeGame.getGameInstance();
 *
 * Harry Felton - 18032692
 */

import controllers.CollisionController;
import controllers.EffectController;
import controllers.EntityController;
import controllers.UIController;
import entity.ApplePickup;
import exception.InvalidParameterException;
import fragment.DeathFragment;
import fragment.GameFragment;
import fragment.MenuFragment;
import fragment.PauseFragment;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class SnakeGame extends GameEngine {
    public static final String TITLE = "Snake Game";
    public static final int    WIDTH = 550;
    public static final int    HEIGHT = WIDTH / 12 * 9;
    public enum STATE {
        MENU,
        GAME,
        DEATH
    }

    public final BufferedImage appleImage;
    public final BufferedImage snakeHeadImage;
    public final BufferedImage snakeBodyImage;

    protected int playerCount = 0;
    protected Player[] players;
    protected STATE gameState = STATE.MENU;
    protected STATE nextState;
    protected boolean paused = false;
    protected Random randomGenerator;

    protected UIController ui;
    protected EffectController fx;
    protected CollisionController collision;
    protected EntityController entity;

    private boolean isGraphicsInitialised = false;

    protected MenuFragment menuFragment;
    protected PauseFragment pauseFragment;
    protected DeathFragment deathFragment;
    protected GameFragment gameFragment;

    protected static SnakeGame gameInstance;

    // Private constructor as this class is a singleton;
    private SnakeGame() {
        super(WIDTH, HEIGHT, TITLE);

        appleImage = loadImage("resources/apple.png");
        snakeHeadImage = loadImage("resources/head.png");
        snakeBodyImage = loadImage("resources/dot.png");
    }

    public static void main(String[] args) {
        // Entry point for game. Use this method to retrieve the singleton instance of SnakeGame.
        SnakeGame.getGameInstance();
    }

    @Override
    public void init() {
        super.init();

        ui = new UIController(this);
        entity = new EntityController(this);
        collision = new CollisionController(this);
        fx = new EffectController(this);
    }

    protected void graphicsReady() {
        if( !isGraphicsInitialised ) return;

        menuFragment =  (MenuFragment)ui.registerFragment(new MenuFragment(this));
        deathFragment = (DeathFragment)ui.registerFragment(new DeathFragment(this));
        pauseFragment = (PauseFragment)ui.registerFragment(new PauseFragment(this));
        gameFragment =  (GameFragment)ui.registerFragment(new GameFragment(this));

        changeGameState(STATE.MENU);
    }

    @Override
    public void update(double dt) {
        if(nextState != null) {
            changeGameState(nextState);
            nextState = null;
        }

        if(paused)
            pauseFragment.activate();

        if(gameState == STATE.GAME && !paused) {
            entity.update(dt);
            fx.update(dt);
        }

        ui.update(dt);
    }

    @Override
    public void paintComponent() {
        if( !isGraphicsInitialised ) {
            isGraphicsInitialised = true;
            graphicsReady();
        }

        changeBackgroundColor(black);
        clearBackground(WIDTH, HEIGHT);

        if(gameState == STATE.GAME) {
            entity.redraw();
            fx.redraw();
        }

        ui.redraw();
    }

    // Called whenever a key is pressed
    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE && gameState == STATE.GAME) {
            paused = !paused;
            // Refresh fragment-based scenes
            changeGameState(gameState);
        } else if (!paused) {
            entity.keyPressed(event);
        }
    }

    // Called whenever a mouse button is pressed
    @Override
    public void mousePressed(MouseEvent event) {
        ui.mousePressed(event);
    }

    // Called whenever a mouse button is released
    @Override
    public void mouseReleased(MouseEvent event) {
        ui.mouseReleased(event);
    }

    // Called whenever the mouse is moved
    @Override
    public void mouseMoved(MouseEvent event) {
        ui.mouseMoved(event);
    }

    public void changeGameState(STATE s, boolean resetUi) {
        if(resetUi) ui.deactivateAllFragments();
        if(s == STATE.MENU) {
            menuFragment.activate();
        } else if(s == STATE.DEATH) {
            deathFragment.activate();
        } else if(s == STATE.GAME) {
            gameFragment.activate();
        }

        gameState = s;
    }

    public void changeGameState(STATE s) {
        this.changeGameState(s, true);
    }

    public void scheduleGameStateChange(STATE s) {
        nextState = s;
    }

    public void snakeDeath(Player player) {
        player.notifyLoss();

        // Allow to finish the entity update cycle incase the snakes crashed in to each other.
        scheduleGameStateChange(STATE.DEATH);
    }

    public Player[] getPlayers() {
        return players;
    }

    public void startGame(int c) {
        playerCount = c;
        players = new Player[c];
        for(int i = 0; i < c; i++) players[i] = new Player(i);

        entity.initWithPlayers(players);
        respawnApple();

        scheduleGameStateChange(STATE.GAME);
    }

    public void respawnApple() {
        entity.spawnPickupRandom(new ApplePickup(this));
    }

    public void quitGame() {
        System.exit(1);
    }

    public Random generateRandom() {
        if(randomGenerator == null)
            randomGenerator = new Random();

        return randomGenerator;
    }

    public Point generateRandomPoint() {
        Random r = generateRandom();
        return new Point(r.nextInt(WIDTH), r.nextInt(HEIGHT));
    }


    /*
     * Returns the SnakeGame singleton instance, prevents having multiple instances of this game running at once.
     */
    public static SnakeGame getGameInstance() {
        // No game? No problem. Create a game instance and store it inside out protected static var.
        if( gameInstance == null ) {
            gameInstance = new SnakeGame();
            SnakeGame.createGame(gameInstance, 30);
        }

        // Return the newly created/previously existing game.
        return gameInstance;
    }

    public Graphics2D getGameGraphics() {
        return mGraphics;
    }

    public UIController getUIController() {
        return ui;
    }

    public CollisionController getCollisionController() {
        return collision;
    }

    public EntityController getEntityController() {
        return entity;
    }

    public EffectController getEffectsController() {
        return fx;
    }
}
