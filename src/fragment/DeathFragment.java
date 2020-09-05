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

/**
 * The DeathFragment is displayed when the game has ended. It shows the winning player and their score.
 *
 * @author Harry Felton
 */
public class DeathFragment extends Fragment {
    /**
     * The text displaying the information of the winning player
     */
    protected Text gameResultText;

    /**
     * The label responsible for displaying the information of the winning player
     */
    protected Label gameResultLabel;

    /**
     * Constructs the Fragment
     *
     * @param game The game instance the fragment is attached to
     */
    public DeathFragment(SnakeGame game) {
        super(game);
    }

    /**
     * Creates the components for display when the fragment becomes active
     */
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

    /**
     * Updates the fragment to display new information about the winning players
     *
     * @param dt Time since the last update occurred
     */
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
