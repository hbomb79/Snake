package ui;

import interfaces.UIMouseReactive;
import main.SnakeGame;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Button extends Component implements UIMouseReactive {
    protected Text text;
    protected HORIZONTAL_TEXT_ALIGN hAlign = HORIZONTAL_TEXT_ALIGN.CENTER;
    protected VERTICAL_TEXT_ALIGN   vAlign = VERTICAL_TEXT_ALIGN.CENTER;

    protected Color bgColour;
    protected Color fgColour;
    protected Color hoveredBgColour;
    protected Color hoveredFgColour;
    protected Color activeBgColour;
    protected Color activeFgColour;

    protected int padding = 10;

    protected Runnable activeCallback;

    protected enum HORIZONTAL_TEXT_ALIGN {
        LEFT,
        CENTER,
        RIGHT
    }
    protected enum VERTICAL_TEXT_ALIGN {
        LEFT,
        CENTER,
        RIGHT
    }

    protected STATE state = STATE.INACTIVE;
    protected enum STATE {
        INACTIVE,
        HOVERED,
        ACTIVE
    }

    public Button(SnakeGame g, double X, double Y, double W, double H, Text t) {
        super(g, X, Y, W, H);
        text = t;

        bgColour = Color.black;
        fgColour = Color.white;
        hoveredBgColour = Color.white;
        hoveredFgColour = Color.white;
        activeBgColour = Color.white;
        activeFgColour = Color.white;
    }

    public Button(SnakeGame g, double X, double Y, Text t) {
        this(g, X, Y, 0, 0, t);
    }

    public Button(SnakeGame g, Text t) {
        this(g, 0, 0, t);
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        state = STATE.HOVERED;
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        state = STATE.INACTIVE;
    }

    @Override
    public void onMouseDown(MouseEvent event, boolean within) {
        if(!within) {
            state = STATE.INACTIVE;
            return;
        }

        state = STATE.ACTIVE;
    }

    @Override
    public void onMouseUp(MouseEvent event, boolean within) {
        // if mouse up within the bounds of the button (might not be, we're told anyway incase the button
        // thinks it's still being pressed; for example when someone clicks, drags the mouse off the button, and then
        // releases), and the button state is active, fire the callback.

        if(within && state == STATE.ACTIVE ) {
            if(activeCallback != null) {
                activeCallback.run();
            }
        }
    }

    @Override
    public boolean isEntered() {
        return state == STATE.HOVERED;
    }

    // No need to update every tick; we only need to update when a mouse event occurs.
    @Override
    public void update(double dt) {}

    @Override
    public void paintComponent() {
        // Figure out what colours we're using based on the 'state'.
        Color fg, bg;
        switch(state){
            case HOVERED -> {
                fg = hoveredFgColour;
                bg = hoveredBgColour;
            }
            case ACTIVE -> {
                fg = activeFgColour;
                bg = activeBgColour;
            }
            default -> {
                fg = fgColour;
                bg = bgColour;
            }
        }

        // Draw the button
        Graphics2D g = gameInstance.getGameGraphics();
        g.setColor(bg);
        g.drawRect((int)x, (int)y, (int)getWidth(), (int)getHeight());

        g.setColor(fg);
        g.setFont(text.getFontInstance());
        g.drawString( text.getText(), (int)x + padding, (int)y + g.getFontMetrics().getHeight() + (int)(padding*0.5) );
    }

    public Button setPadding( int p ) {
        padding = p;
        return this;
    }

    public Component setWidth( double a ) {
        width = a + (2*padding);
        return this;
    }

    public double getWidth() {
        return text.getRenderedWidth() + (2*padding);
    }

    public Component setHeight( double b ) {
        height = b + (2*padding);
        return this;
    }

    public double getHeight() {
        return text.getRenderedHeight() + (2*padding);
    }

    public int getPadding() {
        return padding;
    }

    public Button setBgColour(Color c) {
        return this;
    }

    public Color getBgColour() {
        return bgColour;
    }

    public Button setFgColour(Color c) {
        return this;
    }
    public Color getFgColour() {
        return fgColour;
    }

    public Button setHoveredBgColour(Color c) {
        return this;
    }

    public Color getHoveredBgColour() {
        return hoveredBgColour;
    }

    public Button setHoveredFgColour(Color c) {
        return this;
    }

    public Color getHoveredFgColour() {
        return hoveredFgColour;
    }

    public Button setActiveBgColour(Color c) {
        return this;
    }

    public Color getActiveBgColour() {
        return activeBgColour;
    }

    public Button setActiveFgColour(Color c) {

        return this;
    }

    public Color getActiveFgColour() {
        return activeFgColour;
    }

    public Button setCallback( Runnable r ) {
        activeCallback = r;
        return this;
    }

    public Runnable getCallback() {
        return activeCallback;
    }

    @Override
    public Button center(boolean horizontal, boolean vertical, int xOffset, int yOffset) {
        return (Button)super.center(horizontal, vertical, xOffset, yOffset);
    }
}
