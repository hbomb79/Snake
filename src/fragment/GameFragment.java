package fragment;

import main.Player;
import main.SnakeGame;
import ui.Component;
import ui.Label;
import ui.Text;

import java.awt.*;

/**
 * This fragment is used to display the current score of the players
 *
 * @author Harry Felton
 */
public class GameFragment extends Fragment {
    /**
     * The label used to display the score of player one
     */
    protected Label playerOneScoreLabel;

    /**
     * The label used to display the score of player two
     */
    protected Label playerTwoScoreLabel;

    /**
     * The amount of time that must pass before the score effect resets
     */
    protected final int scoreEffectTransitionTime = 200;

    /**
     * The color to be used during the score effect
     */
    protected final Color scoreEffectColour = new Color(224, 192, 68);

    /**
     * The color to be used when the score effect is not running
     */
    protected final Color scoreBaseColour = new Color(191, 191, 191);

    /**
     * Construct the GameFragment
     *
     * @param g The {@code SnakeGame} instance this Fragment is drawing to
     */
    public GameFragment(SnakeGame g) {
        super(g);
    }

    /**
     * Creates the components to display the score
     */
    @Override
    public void createComponents() {
        super.createComponents();

        playerOneScoreLabel = new Label(gameInstance, new Text("").setSize(14));
        playerTwoScoreLabel = new Label(gameInstance, new Text("").setSize(14));
        components = new Component[] { playerOneScoreLabel, playerTwoScoreLabel };
    }

    /**
     * Calculates the color to use for the score label based on how long ago the player scored a point.
     *
     * @param elapsed Amount of time since the player last scored
     * @return Returns the color to be used
     */
    private Color getScoreColour(double elapsed) {
        double ratio = Math.min(1, (elapsed * 1000) / scoreEffectTransitionTime); // Range from 0.0 to 1.0

        int deltaRed = fadeColorComponent(scoreBaseColour.getRed(), scoreEffectColour.getRed(), ratio);
        int deltaGreen = fadeColorComponent(scoreBaseColour.getGreen(), scoreEffectColour.getGreen(), ratio);
        int deltaBlue = fadeColorComponent(scoreBaseColour.getBlue(), scoreEffectColour.getBlue(), ratio);

        return new Color(deltaRed, deltaGreen, deltaBlue);
    }

    /**
     * Update the fragment by updating the score labels and changing the colour of the label when a player recently
     * scored.
     *
     * @param dt Time passed since last update
     */
    @Override
    public void update(double dt) {
        if(!active) return;
        super.update(dt);

        Player[] players = gameInstance.getPlayers();

        Text playerOneText = playerOneScoreLabel.getText();
        Text playerTwoText = playerTwoScoreLabel.getText();
        playerOneText.setText(players.length > 0 ? String.format("%04d", players[0].getScore()) : "");
        playerTwoText.setText(players.length > 1 ? String.format("%04d", players[1].getScore()) : "");

        playerOneScoreLabel.setX(0).setY(playerOneText.getRenderedHeight());
        playerTwoScoreLabel.setX(SnakeGame.WIDTH - playerTwoScoreLabel.getWidth()).setY(playerTwoText.getRenderedHeight());

        if(players.length > 0) playerOneScoreLabel.setColor(getScoreColour(players[0].getScoreTime()));
        if(players.length > 1) playerTwoScoreLabel.setColor(getScoreColour(players[1].getScoreTime()));
    }

    /**
     * Given two colour components, {@code comp1} and {@code comp2}, find a mix between the two based on the
     * {@code ratio}. If {@code ratio} is 0, then the color returned will be all {@code comp1}. If {@code ratio} is 1,
     * then the returned color will be entirely made up of {@code comp2}.
     *
     * @param comp1 First colour component to mix
     * @param comp2 Second colour component to mix
     * @param ratio Double between 0-1 representing the mix of the colours
     * @return Returns the colour calculated
     */
    private int fadeColorComponent(int comp1, int comp2, double ratio) {
        return (int)Math.abs((ratio*comp1) + ((1-ratio) * comp2));
    }
}
