package entity;

import interfaces.EngineComponent;
import main.SnakeGame;

import java.awt.event.KeyEvent;

public abstract class Entity implements EngineComponent {
    protected SnakeGame gameInstance;

    public Entity(SnakeGame game) {
        gameInstance = game;
    }
}
