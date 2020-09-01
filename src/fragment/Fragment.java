package fragment;

import main.SnakeGame;
import ui.Component;

public abstract class Fragment {
    SnakeGame gameInstance;
    protected Component[] components;

    public Fragment(SnakeGame g) {
        gameInstance = g;
        createComponents();
    }

    public void createComponents() {
        components = new Component[0];
    };

    public void deliverUI() {
        gameInstance.getUIController().populate( components );
    }
}
