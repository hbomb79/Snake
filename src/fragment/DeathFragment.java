package fragment;

import main.SnakeGame;
import ui.Button;
import ui.Component;
import ui.Text;

public class DeathFragment extends Fragment {
    protected int winningPlayer;
    public DeathFragment(SnakeGame game) {
        super(game);
    }

    @Override
    public void createComponents() {
        super.createComponents();

        Text menuText = new Text("Menu");
        Text quitText = new Text("Quit");

        Runnable backToMenu = () -> gameInstance.scheduleGameStateChange(SnakeGame.STATE.MENU);

        components = new Component[]{
                Button.createCentered(gameInstance, menuText, true, true, 0,-100).setCallback(backToMenu),
                Button.createCentered(gameInstance, quitText, true, true, 0, 0).setCallback(gameInstance::quitGame)
        };
    }

    public void providePlayerId(int playerId) {
        winningPlayer = playerId;
    }
}
