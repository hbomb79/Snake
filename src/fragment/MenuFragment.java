package fragment;

import main.SnakeGame;
import ui.Button;
import ui.Component;
import ui.Label;
import ui.Text;

import java.awt.*;

/**
 * This fragment is used to display the main menu
 *
 * @author Harry Felton - 18032692
 */
public class MenuFragment extends Fragment {
    /**
     * Constructs the fragment
     * @param game The {@code SnakeGame} the fragment is attached to
     */
    public MenuFragment(SnakeGame game) {
        super(game);
    }

    /**
     * Create the components to be displayed
     */
    @Override
    public void createComponents() {
        super.createComponents();

        Text spText = new Text("Single Player");
        Text mpText = new Text("Two Player");
        Text quitText = new Text("Quit");

        Runnable startSinglePlayer = () -> gameInstance.startGame(1);
        Runnable startTwoPlayer = () -> gameInstance.startGame(2);

        components = new Component[]{
                new Label(gameInstance, new Text("Snake!").setSize(30)).center(true, true, 0, -100).setColor(Color.green),
                new Button(gameInstance, spText).center(true, true, 0,-50).setCallback(startSinglePlayer),
                new Button(gameInstance, mpText).center(true, true, 0, 0).setCallback(startTwoPlayer),
                new Button(gameInstance, quitText).center(true, true, 0, 50).setCallback(gameInstance::quitGame)
        };
    }
}
