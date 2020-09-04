package fragment;

import main.Player;
import main.SnakeGame;
import ui.Component;
import ui.Label;
import ui.Text;

import java.awt.*;

public class GameFragment extends Fragment {
    protected Label playerOneScoreLabel;
    protected Label playerTwoScoreLabel;

    protected final int scoreEffectTransitionTime = 200;
    protected final Color scoreEffectColour = new Color(224, 192, 68);
    protected final Color scoreBaseColour = new Color(191, 191, 191);

    public GameFragment(SnakeGame g) {
        super(g);
    }

    @Override
    public void createComponents() {
        super.createComponents();

        playerOneScoreLabel = new Label(gameInstance, new Text("").setSize(14));
        playerTwoScoreLabel = new Label(gameInstance, new Text("").setSize(14));
        components = new Component[] { playerOneScoreLabel, playerTwoScoreLabel };
    }

    private Color getScoreColour(double elapsed) {
        double ratio = Math.min(1, (elapsed * 1000) / scoreEffectTransitionTime); // Range from 0.0 to 1.0

        int deltaRed = fadeColorComponent(scoreBaseColour.getRed(), scoreEffectColour.getRed(), ratio);
        int deltaGreen = fadeColorComponent(scoreBaseColour.getGreen(), scoreEffectColour.getGreen(), ratio);
        int deltaBlue = fadeColorComponent(scoreBaseColour.getBlue(), scoreEffectColour.getBlue(), ratio);

        return new Color(deltaRed, deltaGreen, deltaBlue);
    }

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

    private int fadeColorComponent(int comp1, int comp2, double ratio) {
        return (int)Math.abs((ratio*comp1) + ((1-ratio) * comp2));
    }
}
