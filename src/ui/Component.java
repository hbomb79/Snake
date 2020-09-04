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

    public Component center(boolean horizontal, boolean vertical, int xOffset, int yOffset) {
        int frameWidth = SnakeGame.WIDTH;
        int frameHeight = SnakeGame.HEIGHT;
        double dX = xOffset;
        double dY = yOffset;

        // This function assumes the frame of reference is the entire game window, and thus the max width and height
        // is sourced straight from the game instance.
        if( horizontal ) {
            dX = dX + (frameWidth / 2.0) - (getWidth() / 2.0);
        }

        if( vertical ) {
            dY = dY + (frameHeight / 2.0) - (getHeight() / 2.0);
        }

        this.x = dX;
        this.y = dY;

        return this;
    }
}
