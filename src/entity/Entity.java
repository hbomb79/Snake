package entity;

import interfaces.CollisionElement;
import interfaces.EngineComponent;
import main.SnakeGame;

public abstract class Entity implements EngineComponent, CollisionElement {
    protected SnakeGame gameInstance;

    public Entity(SnakeGame game) {
        gameInstance = game;
    }
}
