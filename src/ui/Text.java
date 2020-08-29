package ui;

import main.SnakeGame;

import java.awt.*;

/*
 * A simple class that allows the encapsulation of text information for easier transport of this information
 * inside of classes. For example, a Button class can simply accept a Text object, rather than two strings and an int.
 */
public class Text {
    protected static final String   DEFAULT_FONT = "Arial";
    protected static final int      DEFAULT_SIZE = 20;
    protected static final int      DEFAULT_STYLE = Font.PLAIN;

    protected String text;
    protected String font;
    protected int size;
    protected int style;

    public Text(String t, String f, int z, int s) {
        text = t;
        font = f;
        size = z;
        style = s;
    }

    public Text(String t, String f, int z) {
        this(t, f, z, DEFAULT_STYLE);
    }

    public Text(String t, String f) {
        this(t, f, DEFAULT_SIZE, DEFAULT_STYLE);
    }

    public Text(String t) {
        this(t, DEFAULT_FONT, DEFAULT_SIZE, DEFAULT_STYLE);
    }

    public Text setText(String t) {
        text = t;
        return this;
    }

    public String getText() {
        return text;
    }

    public Text setFont(String f) {
        font = f;
        return this;
    }

    public String getFont() {
        return font;
    }

    public Text setSize(int s) {
        size = s;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Font getFontInstance() {
        return new Font(font, style, size);
    }

    public int getRenderedWidth() {
        // Get the graphics renderer from our engine
        Graphics g = SnakeGame.getGameGraphics();

        // Store the old font here for later
        Font oldFont = g.getFont();
        // Set the font used by the renderer to the one specified by this Text instance
        g.setFont(new Font(font, Font.PLAIN, size));
        // Find the width
        int width = g.getFontMetrics().stringWidth(text);
        // Restore the font
        g.setFont(oldFont);

        return width;
    }

    public int getRenderedHeight() {
        // Get the graphics renderer from our engine
        Graphics g = SnakeGame.getGameGraphics();

        // Store the old font here for later
        Font oldFont = g.getFont();
        // Set the font used by the renderer to the one specified by this Text instance
        g.setFont(new Font(font, Font.PLAIN, size));
        // Find the height
        int height = g.getFontMetrics().getHeight();
        // Restore the font
        g.setFont(oldFont);

        return height;
    }
}
