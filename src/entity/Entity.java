package entity;

import interfaces.EngineComponent;
import main.SnakeGame;

public abstract class Entity implements EngineComponent {
    protected SnakeGame gameInstance;

    protected double xVelocity = 0;
    protected double yVelocity = 0;

    public Entity(SnakeGame game) {
        gameInstance = game;
    }
}
