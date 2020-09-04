package ui;

import main.SnakeGame;

import java.awt.*;

public class Label extends ColouredComponent {
    protected Text text;
    public Label(SnakeGame g, double X, double Y, Text t) {
        super(g, X, Y, 0, 0);
        text = t;
    }

    public Label(SnakeGame g, Text t) {
        this(g, 0, 0, t);
    }

    @Override
    public void update(double dt) {}

    @Override
    public void paintComponent() {
        Graphics2D g = gameInstance.getGameGraphics();

        g.setFont(text.getFontInstance());
        g.setColor(color);
        g.drawString(text.getText(), (int)x, (int)y);
    }

    @Override
    public double getWidth() {
        return text.getRenderedWidth();
    }

    @Override
    public double getHeight() {
        return text.getRenderedHeight();
    }

    public Text getText() {
        return text;
    }

    @Override
    public Label center(boolean horizontal, boolean vertical, int xOffset, int yOffset) {
        return (Label)super.center(horizontal, vertical, xOffset, yOffset);
    }
}
