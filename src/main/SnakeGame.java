package main;

import controllers.CollisionController;
import controllers.EffectController;
import controllers.EntityController;
import controllers.UIController;
import entity.ApplePickup;
import fragment.DeathFragment;
import fragment.GameFragment;
import fragment.MenuFragment;
import fragment.PauseFragment;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Base class for this snake game; instantiates all core controllers, game engine, entity sprites and runs the main
 * render loop.
 *
 * Singleton class, fetch running instance using SnakeGame.getGameInstance();
 *
 * @author Harry Felton
 */
public class SnakeGame extends GameEngine {
    /**
     * The title of the window
     */
    public static final String TITLE = "Snake Game";

    /**
     * The width of the game
     */
    public static final int    WIDTH = 550;

    /**
     * The height of the game
     */
    public static final int    HEIGHT = WIDTH / 12 * 9;

    /**
     * The possible game states
     */
    public enum STATE {
        MENU,
        GAME,
        DEATH
    }

    /**
     * The image of the apple
     */
    public final BufferedImage appleImage;

    /**
     * The image representing the head of the snake
     */
    public final BufferedImage snakeHeadImage;

    /**
     * The image representing the body of the snake
     */
    public final BufferedImage snakeBodyImage;


    /**
     * The list of players currently in-game
     */
    protected Player[] players;

    /**
     * The current game state
     */
    protected STATE gameState = STATE.MENU;

    /**
     * The state that the game will be in after the next update tick completes
     */
    protected STATE nextState;

    /**
     * Represents whether or not the game is paused
     */
    protected boolean paused = false;

    /**
     * The random number generator
     */
    protected Random randomGenerator;

    /**
     * The UIController to manage the fragments
     *
     * @see #getUIController()
     */
    protected UIController ui;

    /**
     * The EffectController used to update and display currently running effects
     *
     * @see #getEffectsController()
     */
    protected EffectController fx;

    /**
     * The CollisionController used to test for collisions between entities
     *
     * @see #getCollisionController()
     */
    protected CollisionController collision;

    /**
     * The EntityController used to manage and update on screen entities
     *
     * @see #getEntityController()
     */
    protected EntityController entity;

    /**
     * Used to indicate whether or not the graphics are initialised
     */
    private boolean isGraphicsInitialised = false;

    /**
     * The fragment used when the game is in MENU state
     */
    protected MenuFragment menuFragment;

    /**
     * The fragment used when the game is in GAME state, and is paused
     */
    protected PauseFragment pauseFragment;

    /**
     * The fragment used when the game is over
     */
    protected DeathFragment deathFragment;

    /**
     * The fragment used when the game is in GAME state
     */
    protected GameFragment gameFragment;

    /**
     * The {@code SnakeGame} singleton instance
     *
     * @see #getGameInstance()
     */
    protected static SnakeGame gameInstance;

    /**
     * Private constructor as this class is a singleton and can only be initialised
     * from inside this class
     *
     * @see #getGameInstance()
     */
    private SnakeGame() {
        super(WIDTH, HEIGHT, TITLE);

        appleImage = loadImage("resources/apple.png");
        snakeHeadImage = loadImage("resources/head.png");
        snakeBodyImage = loadImage("resources/dot.png");
    }

    /**
     * The entry point of this game. Creates the singleton instance.
     *
     * @param args The arguments passed to the program
     */
    public static void main(String[] args) {
        // Entry point for game. Use this method to retrieve the singleton instance of SnakeGame.
        SnakeGame.getGameInstance();
    }

    /**
     * Initialise all the controllers for this instance
     */
    @Override
    public void init() {
        super.init();

        ui = new UIController(this);
        entity = new EntityController(this);
        collision = new CollisionController(this);
        fx = new EffectController(this);
    }

    /**
     * When the graphics are ready, this method initialises all the UI fragments
     * and changes the game state to the MENU
     */
    protected void graphicsReady() {
        if( !isGraphicsInitialised ) return;

        menuFragment =  (MenuFragment)ui.registerFragment(new MenuFragment(this));
        deathFragment = (DeathFragment)ui.registerFragment(new DeathFragment(this));
        pauseFragment = (PauseFragment)ui.registerFragment(new PauseFragment(this));
        gameFragment =  (GameFragment)ui.registerFragment(new GameFragment(this));

        changeGameState(STATE.MENU);
    }

    /**
     * Called on each update tick. Changes the state of the game to the next queued state. If the game is
     * paused, the {@code PauseFragment} is activated. If the game state is currently {@code GAME}, and the
     * game isn't paused, then the entities and effects are updated.
     *
     * @param dt The amount of time passed since the last tick
     */
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

    /**
     * Called when the game requests a repaint. If the graphics are ready, and the fragments aren't initialised, initialise
     * them.
     *
     * Clear the game screen, and redraw the entities, effects and UI.
     */
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

    /**
     * Dispatches the key event to the registered entities, unless the key was 'ESCAPE', in which case the game
     * is (un)paused.
     *
     * @param event The KeyEvent to be dispatched
     */
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

    /**
     * Dispatches the MouseEvent to the UI controller
     *
     * @param event The MouseEvent to be dispatched
     */
    @Override
    public void mousePressed(MouseEvent event) {
        ui.mousePressed(event);
    }

    /**
     * Dispatches the MouseEvent to the UI controller
     *
     * @param event The MouseEvent to be dispatched
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        ui.mouseReleased(event);
    }

    /**
     * Dispatches the MouseEvent to the UI controller
     *
     * @param event The MouseEvent to be dispatched
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        ui.mouseMoved(event);
    }

    /**
     * Change the game state to the one provided. Activates the fragments that must be active
     * for the selected state.
     *
     * @param s The new game-state
     */
    public void changeGameState(STATE s) {
        ui.deactivateAllFragments();
        if(s == STATE.MENU) {
            menuFragment.activate();
        } else if(s == STATE.DEATH) {
            deathFragment.activate();
        } else if(s == STATE.GAME) {
            gameFragment.activate();
        }

        gameState = s;
    }

    /**
     * Schedule a game-state change
     *
     * @param s The next game state
     */
    public void scheduleGameStateChange(STATE s) {
        nextState = s;
    }

    /**
     * Notifies this game instance that the player provided has died.
     *
     * @param player The losing player
     */
    public void snakeDeath(Player player) {
        player.notifyLoss();

        // Allow to finish the entity update cycle incase the snakes crashed in to each other.
        scheduleGameStateChange(STATE.DEATH);
    }

    /**
     * Returns the players currently playing
     *
     * @return Return the list of {@code Player} instances
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Start the game by creating the {@code Player} instances, and initialising the {@code EntityController} with the
     * players. Spawn the initial {@code ApplePickup}, and schedule the game-state to change
     *
     * @param c The count of players
     */
    public void startGame(int c) {
        players = new Player[c];
        for(int i = 0; i < c; i++) players[i] = new Player(i);

        entity.initWithPlayers(players);
        respawnApple();

        scheduleGameStateChange(STATE.GAME);
    }

    /**
     * Queue the respawn of another apple
     */
    public void respawnApple() {
        entity.spawnPickupRandom(new ApplePickup(this));
    }

    /**
     * Quit the game
     */
    public void quitGame() {
        System.exit(1);
    }

    /**
     * Create the random number generator
     *
     * @return Returns the random number generator, creating one if one doesn't already exist
     */
    public Random generateRandom() {
        if(randomGenerator == null)
            randomGenerator = new Random();

        return randomGenerator;
    }

    /**
     * Generates a random point within the boundary of the game
     *
     * @return Returns the {@code Point} generated
     */
    public Point generateRandomPoint() {
        Random r = generateRandom();
        return new Point(r.nextInt(WIDTH), r.nextInt(HEIGHT));
    }

    /**
     * Fetches the {@code SnakeGame} singleton instance
     *
     * @return Returns the existing {@code SnakeGame}, or creates one if it doesn't exist yet.
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

    /**
     * Fetch the game graphics from the underlying game engine
     *
     * @return Returns the {@code Graphics2D} instance
     */
    public Graphics2D getGameGraphics() {
        return mGraphics;
    }

    /**
     * Fetch the UI controller from the game instance
     *
     * @return Returns the {@code UIController}
     */
    public UIController getUIController() {
        return ui;
    }

    /**
     * Fetch the collision controller from the game instance
     *
     * @return Returns the {@code CollisionController}
     */
    public CollisionController getCollisionController() {
        return collision;
    }

    /**
     * Fetch the entity controller from the game instance
     *
     * @return Returns the {@code EntityController}
     */
    public EntityController getEntityController() {
        return entity;
    }

    /**
     * Fetch the effects controller from the game instance
     *
     * @return Returns the {@code EffectsController}
     */
    public EffectController getEffectsController() {
        return fx;
    }
}
