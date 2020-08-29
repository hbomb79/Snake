package controllers;

import main.SnakeGame;
import ui.Component;

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
}
