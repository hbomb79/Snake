package entity;

import interfaces.CollisionElement;
import main.SnakeGame;

import java.awt.*;

public class AppleEntity extends Entity {
    public AppleEntity(SnakeGame game) {
        super(game);
    }

    @Override
    public boolean collidedWithGameBoundary(Rectangle collisionBox) {
        return false;
    }

    @Override
    public boolean collidedWithBy(Rectangle collisionBox, CollisionElement source) {
        return false;
    }

    @Override
    public boolean isCollisionBoxIntersecting(Rectangle collision) {
        return false;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void paintComponent() {

    }
}
