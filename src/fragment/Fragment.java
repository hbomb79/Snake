package fragment;

import interfaces.UIMouseReactive;
import main.SnakeGame;
import ui.Component;

import java.awt.event.MouseEvent;

public abstract class Fragment {
    protected SnakeGame gameInstance;
    protected boolean active;
    protected Component[] components;

    public Fragment(SnakeGame g) {
        gameInstance = g;
        createComponents();
    }

    public void createComponents() {
        components = new Component[0];
    };

    public Fragment activate() {
        active = true;
        return this;
    }

    public Fragment deactivate() {
        active = false;
        return this;
    }

    public void update(double dt) {
        if(!active) return;
        for(Component c : components) {
            c.update(dt);
        }
    }

    public void redraw() {
        if(!active) return;
        for(Component c : components) {
            c.paintComponent();
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
        if(!active) return;
        for (Component e : components) {
            // Check the element wants to receive mouse events (does implement UIMouseReactive?)
            if(e instanceof UIMouseReactive) {
                ((UIMouseReactive) e).onMouseDown(event, checkEventWithinComponent(e, event));
            }
        }
    }

    // Called whenever a mouse button is released
    public void mouseReleased(MouseEvent event) {
        if(!active) return;
        for (Component e : components) {
            if(e instanceof UIMouseReactive) {
                ((UIMouseReactive) e).onMouseUp(event, checkEventWithinComponent(e, event));
            }
        }
    }

    // Called whenever the mouse is moved
    public void mouseMoved(MouseEvent event) {
        if(!active) return;
        for (Component e : components) {
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
