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

import java.awt.*;

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

    protected static SnakeGame gameInstance;

    // Private constructor as this class is a singleton;
    private SnakeGame() {
        super(WIDTH, HEIGHT, TITLE);
        ui = new UIController(this);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(WIDTH, HEIGHT);
        changeColor(green);
        drawCenteredText(50,TITLE);
    }

    public static void main(String[] args) {
        // Entry point for game. Use this method to retrieve the singleton instance of SnakeGame.
        SnakeGame game = SnakeGame.getGameInstance();

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

    public static Graphics getGameGraphics() {
        SnakeGame game = getGameInstance();
        return game.mGraphics;
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
