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
import fragment.MenuFragment;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SnakeGame extends GameEngine {
    public static final String TITLE = "Snake Game";
    public static final int    WIDTH = 550;
    public static final int    HEIGHT = WIDTH / 12 * 9;
    public static enum STATE {
        MENU,
        GAME,
        DEATH
    }

    protected int score = 0;
    protected int lives = 4;
    protected int gameSpeed = 1;
    protected int playerCount = 0;
    protected STATE gameState = STATE.MENU;

    protected UIController ui;
    protected EffectController fx;
    protected CollisionController collision;
    protected EntityController entity;

    private boolean isGraphicsInitialised = false;

    protected MenuFragment menu;

    protected static SnakeGame gameInstance;

    // Private constructor as this class is a singleton;
    private SnakeGame() {
        super(WIDTH, HEIGHT, TITLE);
    }

    @Override
    public void init() {
        super.init();
    }

    protected void graphicsReady() {
        if( !isGraphicsInitialised ) return;
        ui = new UIController(this);
        menu = new MenuFragment(this);

        changeGameState(STATE.MENU);
    }

    @Override
    public void update(double dt) {
        if(gameState == STATE.GAME) {
            entity.update(dt);
        }
    }

    @Override
    public void paintComponent() {
        if( !isGraphicsInitialised ) {
            isGraphicsInitialised = true;
            graphicsReady();
        }

        changeBackgroundColor(black);
        clearBackground(WIDTH, HEIGHT);

        ui.redraw();
    }

    public static void main(String[] args) {
        // Entry point for game. Use this method to retrieve the singleton instance of SnakeGame.
        SnakeGame game = SnakeGame.getGameInstance();

    }

    // Called whenever a key is pressed
    public void keyPressed(KeyEvent event) {}

    // Called whenever a key is released
    public void keyReleased(KeyEvent event) {}

    // Called whenever a key is pressed and immediately released
    public void keyTyped(KeyEvent event) {}

    // Called whenever a mouse button is pressed
    public void mousePressed(MouseEvent event) {
        ui.mousePressed(event);
    }

    // Called whenever a mouse button is released
    public void mouseReleased(MouseEvent event) {
        ui.mouseReleased(event);
    }

    // Called whenever the mouse is moved
    public void mouseMoved(MouseEvent event) {
        ui.mouseMoved(event);
    }

    public void changeGameState(STATE s) {
        ui.reset();
        if(s == STATE.MENU) {
            menu.setupUI();
        } else if(s == STATE.GAME) {
            //TODO
        } else if(s == STATE.DEATH) {
            //TODO
        }

        gameState = s;
    }

    public void startGame(int c) {
        playerCount = c;
        entity.initWithPlayers(playerCount);
    }

    public void quitGame() {
        System.exit(1);
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

    /* Graphics Helper Functions */
    public void drawCenteredText(double y, String s) {
        // Draw text on the screen
        double x = calculateCenteredText(s);
        mGraphics.drawString(s, (int)x, (int)y);
    }

    public void drawCenteredText(double y, String s, String font) {
        // Draw text on the screen
        double x = calculateCenteredText(s, font);
        mGraphics.drawString(s, (int)x, (int)y);
    }

    public void drawCenteredText(double y, String s, String font, int size) {
        // Draw text on the screen
        double x = calculateCenteredText(s, font, size);
        mGraphics.drawString(s, (int)x, (int)y);
    }

    private double calculateCenteredText(String s) {
        mGraphics.setFont(new Font("Arial", Font.PLAIN, 40));
        return (WIDTH/2.0) - (mGraphics.getFontMetrics().stringWidth(s)/2.0);
    }

    private double calculateCenteredText(String s, String font) {
        mGraphics.setFont(new Font(font, Font.PLAIN, 40));
        return (WIDTH/2.0) - (mGraphics.getFontMetrics().stringWidth(s)/2.0);
    }

    private double calculateCenteredText(String s, String font, int size) {
        mGraphics.setFont(new Font(font, Font.PLAIN, size));
        return (WIDTH/2.0) - (mGraphics.getFontMetrics().stringWidth(s)/2.0);
    }
}
