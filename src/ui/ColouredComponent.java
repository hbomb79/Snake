package ui;

import main.SnakeGame;

import java.awt.*;

public abstract class ColouredComponent extends Component {
    protected Color color;

    public ColouredComponent(SnakeGame g, double X, double Y, double W, double H) {
        super(g, X, Y, W, H);
    }

    public ColouredComponent setColor(Color c) {
        color = c;
        return this;
    }

    public Color getColor() {
        return color;
    }
}
