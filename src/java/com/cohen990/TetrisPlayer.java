package com.cohen990;

import java.awt.*;

public class TetrisPlayer {
    public Network network;
    public long fitness;

    public TetrisPlayer(){
        network = null;
        fitness = 0;
    }

    public TetrisPlayer(Network network){
        this.network = network;
        fitness = 0;
    }

    @Override
    public String toString() {
        return String.format("My name is %s - fitness %d", hashCode(), fitness);
    }

    public String toLongString(){
        return String.format("fitness: %d\nnetwork: %s", fitness, network);
    }

    public void evaluateFitness(long score, Color[][] well) {
        fitness = score;
        int wellHeight = well[0].length - 2;
        int wellWidth = well.length - 1;
        for(int i = 0; i < wellHeight; i++){
            int rowBalance = 0;
            for(int j = 1; j < wellWidth; j++){
                if(isFilled(well[j][i])){
                    rowBalance ++;
                } else {
                    rowBalance --;
                }
            }

            fitness += squarePreservingSign(rowBalance);
        }

    }

    private long squarePreservingSign(int rowBalance) {
        if(rowBalance == 0){
            return rowBalance;
        }

        return (long) (Math.pow(rowBalance, 2) * (Math.abs(rowBalance)/rowBalance));
    }

    private boolean isFilled(Color color) {
        if(color.getBlue() == color.getGreen() && color.getGreen() == color.getRed() && color.getRed() == 0){
            return false;
        }

        return true;
    }
}
