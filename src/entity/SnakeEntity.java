package entity;

import main.SnakeGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SnakeEntity extends Entity {
    protected final BufferedImage headImage;
    protected final BufferedImage bodyImage;

    protected final int partWidth;
    protected final int partHeight;

    private static class SnakePart {
        int x;
        int y;

        public SnakePart(int X, int Y) {
            x = X;
            y = Y;
        }
    }

    protected LinkedList<SnakePart> snake = new LinkedList<>();

    public SnakeEntity(SnakeGame game) {
        super(game);

        headImage = game.snakeHeadImage;
        bodyImage = game.snakeBodyImage;

        partWidth = headImage.getWidth();
        partHeight = headImage.getHeight();
    }

    public void increaseLength(int amount) {
        SnakePart last = snake.getLast();
        int oX = last.x;
        int oY = last.y;
        for (int i = 0; i < amount; i++) {
            snake.addLast(new SnakePart((oX * amount) + partWidth, (oY * amount) + partHeight));
        }
    }

    @Override
    public void update(double dt) {
        // Move depending on velocity

        // Check for collision with self, other snake, game boundary or pickups
    }

    @Override
    public void paintComponent() {
        // Draw the snake head and tail
    }
}
