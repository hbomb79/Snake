package controllers;

import entity.Entity;
import entity.SnakeEntity;
import interfaces.CollisionElement;
import main.SnakeGame;

import java.awt.*;

public class CollisionController extends Controller {
    EntityController entities;
    public CollisionController(SnakeGame g) {
        super(g);
        entities = g.getEntityController();
    }

    public boolean checkCollision(CollisionElement source, Rectangle collisionBox) {
        System.out.println("Checking for collision inside " + collisionBox.toString() + " from " + source.toString());
        // If the source is colliding with the game boundary
        if(collisionBox.x < 0 || collisionBox.x + collisionBox.width > SnakeGame.WIDTH || collisionBox.y < 0 || collisionBox.y + collisionBox.height > SnakeGame.HEIGHT) {
            if(source.collidedWithGameBoundary( collisionBox )) {
                return true;
            }
        }

        // Check with the entity controller for currently on-map entities
        for(Entity p : entities.entities) {
            System.out.println("Asking " + p + " if this box collides with it");
            if(p.isCollisionBoxIntersecting(collisionBox)) {
                if(p.collidedWithBy(collisionBox, source)) {
                    return true;
                }
            }
        }

        return false;
    }
}
