package fragment;

import main.SnakeGame;
import ui.Button;
import ui.Component;
import ui.Text;


public class MenuFragment {
    protected SnakeGame gameInstance;
    protected Component[] components;

    public MenuFragment(SnakeGame game) {
        gameInstance = game;
        // These components are created once per menu and are handed to the UIController when the menu is shown.

        Text spText = new Text("Single Player");
        Text mpText = new Text("Two Player");
        Text quitText = new Text("Quit");

        components = new Component[]{
            Button.createCentered(game, spText, true, false, 0, 0),
            Button.createCentered(game, mpText, true, false, 0, 50),
            Button.createCentered(game, quitText, true, false, 0, 100)
        };
    }

    public void setupUI() {
        // Hand off components to UIController to insert them in to the event flow
        gameInstance.getUIController().populate( components );
    }
}
