package fragment;

import main.SnakeGame;
import ui.Component;
import ui.Label;
import ui.Panel;
import ui.Text;

import java.awt.*;

public class PauseFragment extends Fragment {
    public PauseFragment(SnakeGame game) {
        super(game);
    }

    @Override
    public void createComponents() {
        Text pauseText = new Text("Game Paused");

        components = new Component[]{
                new Panel(gameInstance, 1, 1, SnakeGame.WIDTH, SnakeGame.HEIGHT).setColor(new Color(0x9E000000, true)),
                Label.createCentered(gameInstance, pauseText, true, true, 0, -100).setColor(Color.green)
        };
    }
}
