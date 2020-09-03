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

    public Component setX( double a ) {
        x = a;
        return this;
    }

    public double getX() {
        return x;
    }

    public Component setY( double b ) {
        y = b;
        return this;
    }

    public double getY() {
        return y;
    }

    public Component setWidth( double a ) {
        width = a;
        return this;
    }

    public double getWidth() {
        return width;
    }

    public Component setHeight( double b ) {
        height = b;
        return this;
    }

    public double getHeight() {
        return height;
    }
}
