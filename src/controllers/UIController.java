package controllers;

import fragment.Fragment;
import main.SnakeGame;

import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class UIController extends Controller {
    protected LinkedList<Fragment> fragments = new LinkedList<>();
    public UIController(SnakeGame g) {
        super(g);
    }

    public Fragment registerFragment(Fragment f) {
        fragments.add(f);
        return f;
    }

    public Fragment removeFragment(Fragment f) {
        fragments.remove(f);
        return f;
    }

    public void deactivateAllFragments() {
        for(Fragment f : fragments) {
            f.deactivate();
        }
    }

    public void update(double dt) {
        for(Fragment f : fragments) {
            f.update(dt);
        }
    }

    public void redraw() {
        for(Fragment f : fragments) {
            f.redraw();
        }
    }

    // Called whenever a mouse button is pressed
    public void mousePressed(MouseEvent event) {
        for (Fragment f : fragments) {
            f.mousePressed(event);
        }
    }

    // Called whenever a mouse button is released
    public void mouseReleased(MouseEvent event) {
        for (Fragment f : fragments) {
            f.mouseReleased(event);
        }
    }

    // Called whenever the mouse is moved
    public void mouseMoved(MouseEvent event) {
        for (Fragment f : fragments) {
            f.mouseMoved(event);
        }
    }
}
