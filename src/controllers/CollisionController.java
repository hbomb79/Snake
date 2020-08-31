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

    public void checkCollision(CollisionElement source, Rectangle collisionBox) {
        // If the source is colliding with the game boundary
        if(collisionBox.x < 0 || collisionBox.x + collisionBox.width > SnakeGame.WIDTH || collisionBox.y < 0 || collisionBox.y + collisionBox.height > SnakeGame.HEIGHT) {
            source.collidedWithGameBoundary( collisionBox );
            return;
        }

        // Check if the source is colliding with another player
        if(entities.getPlayerCount() > 1) {
            // Two player, get the second players snake
            SnakeEntity snake = entities.players[1];
            if(snake.isCollisionBoxIntersecting(collisionBox)) {
                if(snake.collidedWithBy(collisionBox, source)) return;
            }
        }

        // Check with the entity controller for currently on-map entities
        for(Entity p : entities.pickups) {
            if(p.isCollisionBoxIntersecting(collisionBox)) {
                if(p.collidedWithBy(collisionBox, source)) return;
            }
        }
    }
}
