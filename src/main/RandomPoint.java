package main;

import controllers.CollisionController;
import entity.Pickup;
import interfaces.CollisionElement;

import java.awt.*;

public class RandomPoint extends Point implements CollisionElement {
    protected int width;
    protected int height;

    public RandomPoint(Pickup e) {
        super();
        width = e.getWidth();
        height = e.getHeight();
    }

    public void selectPoint() {
        SnakeGame g = SnakeGame.getGameInstance();
        CollisionController c = g.getCollisionController();

        Point p;
        do {
            p = g.generateRandomPoint();
            x = p.x;
            y = p.y;
        } while (c.checkCollision(this, getBounds()));
    }

    @Override
    public boolean collidedWithGameBoundary(Rectangle collisionBox) {
        return true;
    }

    @Override
    public boolean collidedWithBy(Rectangle collisionBox, CollisionElement source, Rectangle infringedBoundary) {
        return false;
    }

    @Override
    public Rectangle isCollisionBoxIntersecting(Rectangle collision) {
        return null;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
