package entity;

import main.SnakeGame;

import java.awt.*;

public abstract class Pickup extends Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean toBeDestroyed = false;

    public Pickup(SnakeGame game, int pX, int pY) {
        super(game);
        x = pX;
        y = pY;

        init();
    }

    public Pickup(SnakeGame game) {
        this(game, 1, 1);
    }

    public void init(){};

    public void applyEffect(Entity e) {
        destroy();
    }

    public void destroy(){
        gameInstance.getEntityController().destroyPickup(this);
        toBeDestroyed = true;
    }

    public Pickup setX(int dX) {
        x = dX;
        return this;
    }

    public int getX() {
        return x;
    }

    public Pickup setY(int dY) {
        y = dY;
        return this;
    }

    public int getY() {
        return y;
    }

    public Pickup setWidth(int w) {
        width = w;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Pickup setHeight(int h) {
        height = h;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle isCollisionBoxIntersecting(Rectangle collision) {
        return getBounds().intersects(collision) ? getBounds() : null;
    }

    public boolean collidedWithGameBoundary(Rectangle collisionBox) { return false; }
    public void update(double dt) {}
}
