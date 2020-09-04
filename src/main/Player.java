package main;

import entity.SnakeEntity;

public class Player {
    private int score = 0;
    private final int id;
    private boolean hasLost = false;


    public Player(int pId) {
        id = pId;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
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
}
