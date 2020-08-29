package controllers;

import main.SnakeGame;

public abstract class Controller {
    protected SnakeGame gameInstance;
    public Controller(SnakeGame g) {
        gameInstance = g;
    }
}
