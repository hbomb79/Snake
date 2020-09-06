package main;

/**
 * The Player class is used to keep track of the player stats
 *
 * @author Harry Felton - 18032692
 */
public class Player {
    /**
     * The players current score
     */
    private int score = 0;

    /**
     * The players ID
     */
    private final int id;

    /**
     * Represents whether or not the player has been disqualified/has lost
     */
    private boolean hasLost = false;

    /**
     * Time passed since the last time this player scored
     */
    private double timeSinceLastScore = 0;

    /**
     * Constructs the player instance
     *
     * @param pId The ID of the player
     */
    public Player(int pId) {
        id = pId;
    }

    /**
     * Increase the score of the player by {@code amount}
     *
     * @param amount The amount by which to increase the players score
     * @see #score
     */
    public void increaseScore(int amount) {
        score += amount;
        timeSinceLastScore = 0;
    }

    /**
     * Returns the score of this player
     *
     * @return Returns the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the time since the player scored a point
     *
     * @return Returns the time elapsed since the player scored
     */
    public double getScoreTime() {
        return timeSinceLastScore;
    }

    /**
     * Get the ID of this player
     *
     * @return Returns the ID of the player
     */
    public int getId() {
        return id;
    }

    /**
     * Notify the player that they've lost
     */
    public void notifyLoss() {
        hasLost = true;
    }

    /**
     * Query if this player is the winner
     *
     * @return Returns true if the player is winning, false otherwise
     */
    public boolean isWinner() {
        return !hasLost;
    }

    /**
     * Update the Player instance
     *
     * @param dt Time since last update
     */
    public void update(double dt) {
        timeSinceLastScore += dt;
    }
}
