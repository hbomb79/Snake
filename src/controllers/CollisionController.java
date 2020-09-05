package controllers;

import entity.Entity;
import interfaces.CollisionElement;
import main.SnakeGame;

import java.awt.*;

/**
 * CollisionController is responsible for checking for collisions between entities running inside the
 * game.
 *
 * @author Harry Felton
 */
public class CollisionController extends Controller {
    EntityController entities;

    /**
     * Instantiate the controller by passing the SnakeGame instance to the super class,
     * and storing a reference to the EntityController from the game.
     *
     * @param g The SnakeGame instance that the {@code CollisionController} belongs to.
     */
    public CollisionController(SnakeGame g) {
        super(g);
        entities = g.getEntityController();
    }

    /**
     * Checks to see if the {@code collisionBox} provided is colliding with any registered elements
     * from {@code EntityController}, including any {@code SnakeEntity} or the boundary of the game.
     *
     * @param source The source of the collision
     * @param collisionBox The collision boundary to be tested
     * @return If a collision occurred
     */
    public boolean checkCollision(CollisionElement source, Rectangle collisionBox) {
        // If the source is colliding with the game boundary
        if(collisionBox.x < 0 || collisionBox.x + collisionBox.width > SnakeGame.WIDTH || collisionBox.y < 0 || collisionBox.y + collisionBox.height > SnakeGame.HEIGHT) {
            if(source.collidedWithGameBoundary( collisionBox )) {
                return true;
            }
        }

        // Check with the entity controller for currently on-map entities
        for(Entity p : entities.entities) {
            Rectangle collidedWith = p.isCollisionBoxIntersecting(collisionBox, source);
            if(collidedWith != null) {
                if(p.collidedWithBy(collisionBox, source, collidedWith)) {
                    return true;
                }
            }
        }

        return false;
    }
}
