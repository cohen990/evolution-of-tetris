package com.cohen990;

public class WeightMap{
    private double[][] weights;
    private double[] biases;
    public final int length;
    public final int biasesLength;

    public WeightMap(double[][] weights, double[] biases) {
        this.weights = weights;
        this.length = weights.length;
        this.biases = biases;
        this.biasesLength = biases.length;
    }

    public double[] getWeights(int i) {
        return weights[i];
    }

    public double getBias(int i) {
        return biases[i];
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
