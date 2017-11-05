package com.cohen990;

public class WeightMap{
    private double[][] weights;

    public WeightMap(double[][] weights) {
        this.weights = weights;
    }

    public double[] get(int i) {
        return weights[i];
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < weights.length; i++){
            for(int j = 0; j < weights[i].length; j++){
                result.append(weights[i][j] + " ");
            }
            result.append("\n");
        }

        return result.toString();
    }
}
