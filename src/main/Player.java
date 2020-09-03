package main;

import entity.SnakeEntity;

public class Player {
    int score = 0;
    SnakeEntity snake;
    int lives = 3;
    int id;

    public Player(int pId) {
        id = pId;
    }

    public void assignSnake(SnakeEntity s) {
        snake = s;
    }
}
