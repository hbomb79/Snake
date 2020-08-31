package controllers;

import entity.Entity;
import entity.SnakeEntity;
import exception.InvalidParameterException;
import main.SnakeGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class EntityController extends Controller {
    SnakeGame gameInstance;

    protected int playerCount;
    protected SnakeEntity[] players;
    protected LinkedList<Entity> pickups = new LinkedList<>();

    public EntityController(SnakeGame g) {
        super(g);
    }

    public void initWithPlayers(int count) throws InvalidParameterException {
        if(count < 1 || count > 2) {
            throw new InvalidParameterException("Cannot init EntityController with player count of: " + count + ". Expected 1 or 2.");
        }
        players = new SnakeEntity[count];
        playerCount = count;

        for(int i = 0; i < count; i++) {
            System.out.println("Inserting snake player in to index: " + i);
            players[i] = new SnakeEntity(gameInstance);
        }

        System.out.println(players[0]);
    }

    public void update(double dt) {
        if(players == null) return;
        for(SnakeEntity player : players) {
            player.update(dt);
        }
    }

    public void redraw() {
        if(players == null) return;
        for(SnakeEntity player : players) {
            player.paintComponent();
        }
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void keyPressed(KeyEvent event) {
        if(players == null) return;
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

            case KeyEvent.VK_ESCAPE:
                //TODO Handle pause menu
                break;
        }
    };

    public void spawnEntity(Entity e, Point p) {

    }
}
