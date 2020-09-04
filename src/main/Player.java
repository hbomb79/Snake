package main;

public class Player {
    private int score = 0;
    private final int id;
    private boolean hasLost = false;
    private double timeSinceLastScore = 0;


    public Player(int pId) {
        id = pId;
    }

    public void increaseScore(int amount) {
        score += amount;
        timeSinceLastScore = 0;
    }

    public int getScore() {
        return score;
    }

    public double getScoreTime() {
        return timeSinceLastScore;
    }

    public int getId() {
        return id;
    }

    public void notifyLoss() {
        System.out.println("Player " + id + " LOST");
        hasLost = true;
    }

    public boolean isWinner() {
        return !hasLost;
    }

    public void update(double dt) {
        timeSinceLastScore += dt;
    }
}
