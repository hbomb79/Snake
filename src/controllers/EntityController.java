package controllers;

import entity.Entity;
import entity.Pickup;
import entity.SnakeEntity;
import exception.InvalidParameterException;
import main.RandomPoint;
import main.SnakeGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class EntityController extends Controller {
    protected int playerCount;
    protected final LinkedList<Entity> entities = new LinkedList<>();
    protected final LinkedList<Entity> entitiesToDestroy = new LinkedList<>();
    protected final LinkedList<Entity> entitiesToSpawn = new LinkedList<>();

    public EntityController(SnakeGame g) {
        super(g);
    }

    public void initWithPlayers(int count) throws InvalidParameterException {
        if(count < 1 || count > 2) {
            throw new InvalidParameterException("Cannot init EntityController with player count of: " + count + ". Expected 1 or 2.");
        }

        entities.clear();
        playerCount = count;
        for(int i = 0; i < count; i++) {
            entities.add(new SnakeEntity(gameInstance, i));
        }
    }

    public void update(double dt) {
        for (Entity entity : entities) {
            System.out.println("Updating " + entity);
            entity.update(dt);
        }

        destroyPickups();
        spawnPickups();
    }

    public void redraw() {
        for(Entity entity : entities) {
            entity.paintComponent();
        }
    }

    protected void destroyPickups() {
        entities.removeAll(entitiesToDestroy);
        entitiesToDestroy.clear();
    }

    protected void spawnPickups() {
        entities.addAll(entitiesToSpawn);
        entitiesToSpawn.clear();
    }

    public SnakeEntity getPlayer(int playerId) {
        if(playerCount < 1 || playerId > playerCount || playerId < 0)
            return null;
        for(Entity entity : entities) {
            if(entity instanceof SnakeEntity && ((SnakeEntity) entity).getPlayerId() == playerId)
                return (SnakeEntity)entity;
        }

        return null;
    }

    public int getPlayerCount() {
        return playerCount;
    }

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
    };

    public void spawnPickupRandom(Pickup e) {
        RandomPoint p = new RandomPoint(e);
        p.selectPoint();
        spawnPickup(e, p);
    }

    public void spawnPickup(Pickup e, Point p) {
        e.setX(p.x);
        e.setY(p.y);

        entitiesToSpawn.add(e);
    }

    public void destroyPickup(Pickup p) {
        entitiesToDestroy.add(p);
    }
}
