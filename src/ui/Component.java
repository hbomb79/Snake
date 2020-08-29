package ui;

import interfaces.EngineComponent;
import main.SnakeGame;

public abstract class Component implements EngineComponent {
    protected SnakeGame gameInstance;
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public Component(SnakeGame g, double X, double Y, double W, double H) {
        gameInstance = g;
        x = X;
        y = Y;
        width = W;
        height = H;
    }
}
