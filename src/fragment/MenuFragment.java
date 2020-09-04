package fragment;

import main.SnakeGame;
import ui.Button;
import ui.Component;
import ui.Text;

public class MenuFragment extends Fragment {
    public MenuFragment(SnakeGame game) {
        super(game);
    }

    @Override
    public void createComponents() {
        super.createComponents();

        Text spText = new Text("Single Player");
        Text mpText = new Text("Two Player");
        Text quitText = new Text("Quit");

        Runnable startSinglePlayer = () -> gameInstance.startGame(1);
        Runnable startTwoPlayer = () -> gameInstance.startGame(2);

        components = new Component[]{
                new Button(gameInstance, spText).center(true, true, 0,-100).setCallback(startSinglePlayer),
                new Button(gameInstance, mpText).center(true, true, 0, -50).setCallback(startTwoPlayer),
                new Button(gameInstance, quitText).center(true, true, 0, 0).setCallback(gameInstance::quitGame)
        };
    }
}
