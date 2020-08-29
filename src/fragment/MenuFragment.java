package fragment;

import main.SnakeGame;
import ui.Button;
import ui.Component;
import ui.Text;

import java.util.Arrays;


public class MenuFragment {
    protected SnakeGame gameInstance;
    protected Component[] components;

    public MenuFragment(SnakeGame game) {
        gameInstance = game;
        // These components are created once per menu and are handed to the UIController when the menu is shown.

        Text spText = new Text("Single Player");
        Text mpText = new Text("Two Player");
        Text quitText = new Text("Quit");

        Runnable startSinglePlayer = () -> {
            game.startGame(1);
        };

        Runnable startTwoPlayer = () -> {
            game.startGame(2);
        };

        Runnable quit = () -> {
            game.quitGame();
        };

        components = new Component[]{
            Button.createCentered(game, spText, true, true, 0,-100).setCallback(startSinglePlayer),
            Button.createCentered(game, mpText, true, true, 0, -50).setCallback(startTwoPlayer),
            Button.createCentered(game, quitText, true, true, 0, 0).setCallback(quit)
        };
    }

    public void setupUI() {
        // Hand off components to UIController to insert them in to the event flow
        gameInstance.getUIController().populate( components );
    }
}
