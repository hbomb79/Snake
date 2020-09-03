package ui;

import main.SnakeGame;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Panel extends ColouredComponent {
    boolean filled = true;
    public Panel(SnakeGame g, double X, double Y, double W, double H) {
        super(g, X, Y, W, H);
    }

    @Override
    public void update(double dt) {}

    @Override
    public void paintComponent() {
        Graphics2D g = gameInstance.getGameGraphics();

        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
        g.setColor(color);
        if(filled) {
            g.fill(rect);
        } else {
            g.draw(rect);
        }
    }
}
