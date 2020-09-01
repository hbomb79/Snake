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
                Button.createCentered(gameInstance, spText, true, true, 0,-100).setCallback(startSinglePlayer),
                Button.createCentered(gameInstance, mpText, true, true, 0, -50).setCallback(startTwoPlayer),
                Button.createCentered(gameInstance, quitText, true, true, 0, 0).setCallback(gameInstance::quitGame)
        };
    }
}
