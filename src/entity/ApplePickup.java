package entity;

import effects.TextFadeEffect;
import interfaces.CollisionElement;
import main.SnakeGame;
import ui.Text;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ApplePickup class exists inside the game, and when collided with by a {@code SnakeEntity}, applies an effect to it,
 * such as increasing it's length
 * 
 * @author HarryFelton
 * @see #applyEffect(Entity)
 */
public class ApplePickup extends Pickup {
    /**
     * The image of the apple to be displayed in-game
     */
    BufferedImage appleImage;

    /**
     * Constructs the {@code ApplePickup} with the {@code SnakeGame} instance provided
     *
     * @param game {@code SnakeGame} instance the pickup is registered to
     * @param x The x position of the {@code Pickup}
     * @param y The y position of the {@code Pickup}
     */
    public ApplePickup(SnakeGame game, int x, int y) {
        super(game, x, y);
    }

    /**
     * Constructs the {@code ApplePickup}, without providing the x,y position
     *
     * @param game The {@code SnakeGame} instance the pickup is registered to
     */
    public ApplePickup(SnakeGame game) {
        super(game);
    }

    /**
     * Called by {@code Pickup} when the pre-construction of the instance is ready.
     */
    @Override
    public void init() {
        appleImage = gameInstance.appleImage;
        width = appleImage.getWidth();
        height = appleImage.getHeight();
    }

    /**
     * Applies the effect of this pickup to the {@code Entity} provided. This pickup will be destroyed,
     * and the {@code SnakeGame} will be told to respawn another ApplePickup.
     *
     * @param e The {@code Entity} that the pickup is being applied to
     */
    @Override
    public void applyEffect(Entity e) {
        // Super will destroy the pickup
        super.applyEffect(e);
        // Inform the game to create another apple
        gameInstance.respawnApple();

        // We will apply the increased length to the snake
        if(e instanceof SnakeEntity) {
            SnakeEntity snake = (SnakeEntity) e;
            snake.increaseLength(1);
            snake.getPlayer().increaseScore(10);

            // Spawn a text effect
            Text t = new Text("+10", "Arial", 14);
            TextFadeEffect fx = new TextFadeEffect(x, y, t, new Color(224, 192, 68), 10);
            gameInstance.getEffectsController().spawnEffect(fx);
        }
    }

    /**
     * Called when the {@code CollisionController} attached to this pickups {@code SnakeGame} instance detects
     * that an entity has collided with this pickup. If a {@code SnakeEntity} has collided with this pickup, apply
     * the effect to it and consume the collision.
     *
     * @param collisionBox The collision box that tripped this collision callback
     * @param source The source of the collision
     * @param infringedBoundary The boundary of this pickup that was collided with
     * @return Returns true if the collision has been consumed, false otherwise
     */
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

    /**
     * Draw the pickup on screen
     */
    @Override
    public void paintComponent() {
        gameInstance.drawImage(appleImage, x, y);
    }
}
