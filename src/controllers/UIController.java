package controllers;

import interfaces.UIMouseReactive;
import main.SnakeGame;
import ui.Component;

import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class UIController extends Controller {
    protected LinkedList<Component> elements = new LinkedList<>();
    public UIController(SnakeGame g) {
        super(g);
    }

    public void populate(Component[] e) {
        Collection<Component> c = Arrays.asList(e);
        elements.addAll(c);
    }

    public void reset() {
        elements.clear();
    }

    public void redraw() {
        for (Component e : elements) {
            // Ask the element to paint itself
            e.paintComponent();
        }
    }

    // We only require mouse events to be dispatched to UI elements at the moment (no element uses keyboard input)

    private boolean checkEventWithinComponent(Component c, MouseEvent event) {
        int elementX, elementY, eventX, eventY;
        elementX = (int)c.getX();
        elementY = (int)c.getY();
        eventX = event.getX();
        eventY = event.getY();
        return (eventX > elementX && eventX < elementX + c.getWidth()) && (eventY > elementY && eventY < elementY + c.getHeight());
    }
    // Called whenever a mouse button is pressed
    public void mousePressed(MouseEvent event) {
        for (Component e : elements) {
            // Check the element wants to receive mouse events (does implement UIMouseReactive?)
            if(e instanceof UIMouseReactive) {
                ((UIMouseReactive) e).onMouseDown(event, checkEventWithinComponent(e, event));
            }
        }
    }

    // Called whenever a mouse button is released
    public void mouseReleased(MouseEvent event) {
        for (Component e : elements) {
            if(e instanceof UIMouseReactive) {
                ((UIMouseReactive) e).onMouseUp(event, checkEventWithinComponent(e, event));
            }
        }
    }

    // Called whenever the mouse is moved
    public void mouseMoved(MouseEvent event) {
        for (Component e : elements) {
            if(e instanceof UIMouseReactive) {
                boolean isWithin = checkEventWithinComponent(e, event);
                if(isWithin && !((UIMouseReactive) e).isEntered()) {
                    ((UIMouseReactive) e).onMouseEnter(event);
                } else if(!isWithin && ((UIMouseReactive) e).isEntered()) {
                    ((UIMouseReactive) e).onMouseLeave(event);
                }
            }
        }
    }
}
