package controllers;

import entity.Entity;
import entity.Pickup;
import entity.SnakeEntity;
import main.Player;
import main.RandomPoint;
import main.SnakeGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * The EntityController class handles the adding, removal, updating and drawing of onscreen elements, such
 * as {@code SnakeEntity} and {@code ApplePickup}.
 *
 * @author Harry Felton
 */
public class EntityController extends Controller {
    /**
     * The amount of players registered inside the {@code EntityController}
     */
    protected int playerCount;

    /**
     * The entities currently registered inside this controller; they'll receive requests to update
     * and redraw themselves
     *
     * @see #update(double)
     * @see #redraw()
     */
    protected final LinkedList<Entity> entities = new LinkedList<>();

    /**
     * Entities to be destroyed after the end of the next update cycle
     *
     * @see #destroyPickup(Pickup)
     * @see #destroyPickups()
     */
    protected final LinkedList<Entity> entitiesToDestroy = new LinkedList<>();

    /**
     * Entities to be spawned at the beginning of an update cycle
     *
     * @see #spawnPickup(Pickup, Point)
     * @see #spawnPickups()
     */
    protected final LinkedList<Entity> entitiesToSpawn = new LinkedList<>();

    /**
     * Instantiates the controller with the {@code SnakeGame} instance to be used later
     *
     * @param g The current SnakeGame instace
     */
    public EntityController(SnakeGame g) {
        super(g);
    }

    /**
     * Initiates the {@code EntityController} with the {@code players} provided.
     *
     * @param players The {@code Player} instances to be used when initiating this controller
     */
    public void initWithPlayers(Player[] players) {

        entities.clear();
        playerCount = players.length;
        for(int i = 0; i < playerCount; i++) {
            SnakeEntity e = new SnakeEntity(gameInstance, players[i]);
            entities.add(e);
        }
    }

    /**
     * Spawns any entities that are currently queued, and then send an update tick to all currently registered. After
     * completion, remove any entities that have been queued for removal
     *
     * @param dt Time passed since last tick
     * @see #entities
     * @see #entitiesToSpawn
     * @see #entitiesToDestroy
     */
    public void update(double dt) {
        spawnPickups();
        for (Entity entity : entities) {
            entity.update(dt);
        }
        destroyPickups();
    }

    /**
     * Redraws all entities currently registered
     *
     * @see #entities
     */
    public void redraw() {
        for(Entity entity : entities) {
            entity.paintComponent();
        }
    }

    /**
     * Destroys all entities that have been queued to be removed
     *
     * @see #entitiesToDestroy
     */
    protected void destroyPickups() {
        entities.removeAll(entitiesToDestroy);
        entitiesToDestroy.clear();
    }

    /**
     * Spawns any entities that are currently queued
     *
     * @see #entitiesToSpawn
     */
    protected void spawnPickups() {
        entities.addAll(entitiesToSpawn);
        entitiesToSpawn.clear();
    }

    /**
     * Fetches the {@code SnakeEntity} instance belonging to the {@code Player} with ID {@code playerId}
     *
     * @param playerId The ID of the player connected to the snake we're trying to fetch
     * @return Returns the matching {@code SnakeEntity}, or {@code null} if no match was found
     */
    public SnakeEntity getPlayer(int playerId) {
        if(playerCount < 1 || playerId > playerCount || playerId < 0)
            return null;
        for(Entity entity : entities) {
            if(entity instanceof SnakeEntity && ((SnakeEntity) entity).getPlayerId() == playerId)
                return (SnakeEntity)entity;
        }

        return null;
    }

    /**
     * Handles incoming key presses by dispatching them to the players currently registered
     *
     * @param event The {@code KeyEvent} to be dispatched
     * @see #getPlayer(int)
     */
    public void keyPressed(KeyEvent event) {
        SnakeEntity[] players = {getPlayer(0), getPlayer(1)};
        int keycode = event.getKeyCode();
        int i = 0;
        switch(keycode) {
            // Control UP
            case KeyEvent.VK_UP:
                i = 1;
            case KeyEvent.VK_W:
                players[Math.min(i, playerCount-1)].changeDirection(SnakeEntity.DIRECTION.UP);
                break;

            // Control DOWN
            case KeyEvent.VK_DOWN:
                i = 1;
            case KeyEvent.VK_S:
                players[Math.min(i, playerCount-1)].changeDirection(SnakeEntity.DIRECTION.DOWN);
                break;

            // Control RIGHT
            case KeyEvent.VK_RIGHT:
                i = 1;
            case KeyEvent.VK_D:
                players[Math.min(i, playerCount-1)].changeDirection(SnakeEntity.DIRECTION.RIGHT);
                break;

            // Control LEFT
            case KeyEvent.VK_LEFT:
                i = 1;
            case KeyEvent.VK_A:
                players[Math.min(i, playerCount-1)].changeDirection(SnakeEntity.DIRECTION.LEFT);
                break;
        }
    }

    /**
     * Given a {@code Pickup} instance, this function generates a random point for it, ensuring that
     * the pickup is clear of any other entities inside the game.
     *
     * @param e The {@code Pickup} instance to be spawned
     */
    public void spawnPickupRandom(Pickup e) {
        RandomPoint p = new RandomPoint(e);
        p.selectPoint();
        spawnPickup(e, p);
    }

    /**
     * Spawns the {@code Pickup} instance at the {@code Point} provided by setting the {@code x} and {@code y}
     * position of the {@code Pickup} to that of the {@code Point} provided
     *
     * @param e The {@code Pickup} instance to be spawned
     * @param p The {@code Point} to be used to place the {@code Pickup}
     */
    public void spawnPickup(Pickup e, Point p) {
        e.setX(p.x);
        e.setY(p.y);

        entitiesToSpawn.add(e);
    }

    /**
     * Queues the given {@code Pickup} instance to be destroyed from the controller
     *
     * @param p The {@code Pickup} instance to be removed
     * @see #destroyPickups()
     */
    public void destroyPickup(Pickup p) {
        entitiesToDestroy.add(p);
    }
}
