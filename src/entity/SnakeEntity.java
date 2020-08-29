package entity;

import main.SnakeGame;

import java.awt.*;
import java.util.LinkedList;

public class SnakeEntity extends Entity {
    protected final Image headImage;
    protected final Image bodyImage;

    private static class SnakePart {
        int x;
        int y;

        public SnakePart(int X, int Y) {
            x = X;
            y = Y;
        }

        public boolean checkCollide(){
            //TODO
            return false;
        };
    }

    protected LinkedList<SnakePart> snake = new LinkedList<>();

    public SnakeEntity(SnakeGame game) {
        super(game);

        headImage = game.snakeHeadImage;
        bodyImage = game.snakeBodyImage;
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void paintComponent() {

    }
}
