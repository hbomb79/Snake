package controllers;

import entity.SnakeEntity;
import main.SnakeGame;

public class EntityController {
    SnakeGame gameInstance;

    int playerCount;
    SnakeEntity[] players;

    public EntityController(SnakeGame game) {
        gameInstance = game;
    }

    public void initWithPlayers(int count) {
        players = new SnakeEntity[count];
        playerCount = count;

        for(int i = 0; i < count; i++) {
            players[i] = new SnakeEntity(gameInstance);
        }
    }

    public void update(double dt) {

    }
}
