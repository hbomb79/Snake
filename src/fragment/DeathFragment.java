package fragment;

import main.Player;
import main.SnakeGame;
import ui.Button;
import ui.Component;
import ui.Label;
import ui.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeathFragment extends Fragment {
    protected Text gameResultText;
    protected Label gameResultLabel;
    public DeathFragment(SnakeGame game) {
        super(game);
    }

    @Override
    public void createComponents() {
        super.createComponents();

        Text menuText = new Text("Menu");
        Text quitText = new Text("Quit");
        Text gameOverText = new Text("Game Over");
        gameResultText = new Text("");
        gameResultLabel = new Label(gameInstance, gameResultText);

        Runnable backToMenu = () -> gameInstance.scheduleGameStateChange(SnakeGame.STATE.MENU);

        components = new Component[]{
                new Label(gameInstance, gameOverText).center(true, true, 0, -150),
                gameResultLabel,
                new Button(gameInstance, menuText).center(true, true, 0, -100).setCallback(backToMenu),
                new Button(gameInstance, quitText).center(true, true, 0, 0).setCallback(gameInstance::quitGame)
        };
    }

    @Override
    public void update(double dt) {
        if(!active) return;
        super.update(dt);

        // Find which player has lost.
        Player[] players = gameInstance.getPlayers();
        if(players.length == 1) {
            // Single player
            gameResultText.setText("You got " + players[0].getScore() + " score!");
        } else if(players.length > 1) {
            List<Player> winners = Arrays.stream(players).filter(Player::isWinner).collect(Collectors.toList());

            if (winners.size() == 0) {
                // Tie
                gameResultText.setText("Tie!");
            } else if (winners.size() == 1) {
                Player winner = winners.get(0);
                gameResultText.setText("Player " + (winner.getId() + 1) + " won with " + winner.getScore() + " score!");
            }
        }

        gameResultLabel.center(true, true, 0, -120);
    }
}
