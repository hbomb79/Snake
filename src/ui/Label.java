package ui;

import main.SnakeGame;

import java.awt.*;

public class Label extends ColouredComponent {
    protected Text text;
    public Label(SnakeGame g, double X, double Y, double W, double H, Text t) {
        super(g, X, Y, W, H);
        text = t;
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void paintComponent() {
        Graphics2D g = gameInstance.getGameGraphics();

        System.out.println("Drawing " + text.getText() + " @ x, y: " + x + ", " + y);
        g.setFont(text.getFontInstance());
        g.setColor(color);
        g.drawString(text.getText(), (int)x, (int)y);
    }

    public static Label createCentered(SnakeGame g, Text t, boolean horizontal, boolean vertical, int xOffset, int yOffset) {
        int tWidth = t.getRenderedWidth();
        int tHeight = t.getRenderedHeight();
        int frameWidth = SnakeGame.WIDTH;
        int frameHeight = SnakeGame.HEIGHT;
        double dX = xOffset;
        double dY = yOffset;

        // This function assumes the frame of reference is the entire game window, and thus the max width and height
        // is sourced straight from the game instance.
        if( horizontal ) {
            dX = dX + (frameWidth / 2.0) - (tWidth / 2.0);
        }

        if( vertical ) {
            dY = dY + (frameHeight / 2.0) - (tHeight / 2.0);
        }

        return new Label(g, dX, dY, tWidth, tHeight, t);
    }

    public static Label createCentered(SnakeGame g, Text t, boolean horizontal, boolean vertical) {
        return createCentered(g, t, horizontal, vertical, 0, 0);
    }
}
