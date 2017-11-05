package com.cohen990;

public class TetrisPlayer {
    private Network network;
    public long score;

    public TetrisPlayer(Network network, long score) {
        this.network = network;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("My name is %s - I scored %d", hashCode(), score);
    }

    public String toLongString(){
        return String.format("score: %d\nnetwork: %s", score, network);
    }
}
