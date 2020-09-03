package entity;

import interfaces.CollisionElement;
import main.SnakeGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ApplePickup extends Pickup {
    BufferedImage appleImage;
    public ApplePickup(SnakeGame game, int x, int y) {
        super(game, x, y);
    }

    public ApplePickup(SnakeGame game) {
        super(game);
    }

    @Override
    public void init() {
        appleImage = gameInstance.appleImage;
        width = appleImage.getWidth();
        height = appleImage.getHeight();
    }

    @Override
    public void applyEffect(Entity e) {
        // Super will destroy the pickup
        super.applyEffect(e);

        // We will apply the increased length to the snake
        if(e instanceof SnakeEntity) {
            ((SnakeEntity) e).increaseLength(1);
        }

        // Inform the game to create another apple
        gameInstance.respawnApple();
    }

    @Override
    public boolean collidedWithBy(Rectangle collisionBox, CollisionElement source, Rectangle infringedBoundary) {
        if(source instanceof SnakeEntity) {
            // A snake collided with this pickup, apply our effect to the snake
            applyEffect((SnakeEntity)source);
            return true;
        }

        // Any other collision (such as a RandomPoint) is not relevant; no action required, and we
        // return false to inform the controller that we are not blocking this collision from propagating.
        return false;
    }

    @Override
    public void paintComponent() {
        gameInstance.drawImage(appleImage, x, y);
    }
}
