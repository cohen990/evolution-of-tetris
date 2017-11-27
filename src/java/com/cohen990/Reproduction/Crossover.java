package com.cohen990.Reproduction;

import com.cohen990.Network;
import com.cohen990.TetrisPlayer;
import com.cohen990.WeightMap;

import java.util.Random;

public class Crossover {
    private Random random;

    public Crossover(){
        random = new Random();
    }

    public ChildPair getChildren(TetrisPlayer parent1, TetrisPlayer parent2){
        TetrisPlayer child1 = new TetrisPlayer();
        TetrisPlayer child2 = new TetrisPlayer();

        WeightMapPair inputToHiddenPair = getWeightMaps(parent1.network.inputToHidden1, parent2.network.inputToHidden1);
        WeightMapPair hiddenToOutputPair = getWeightMaps(parent1.network.hidden2ToOutput, parent2.network.hidden2ToOutput);

        child1.network = new Network();
        child1.network.inputToHidden1 = inputToHiddenPair.first;
        child1.network.hidden2ToOutput = hiddenToOutputPair.first;
        child1.network.inputLayer = parent1.network.inputLayer;
        child1.network.hiddenLayer1 = parent1.network.hiddenLayer1;
        child1.network.outputLayer = parent1.network.outputLayer;

        child2.network = new Network();
        child2.network.inputToHidden1 = inputToHiddenPair.second;
        child2.network.hidden2ToOutput = hiddenToOutputPair.second;
        child2.network.inputLayer = parent1.network.inputLayer;
        child2.network.hiddenLayer1 = parent1.network.hiddenLayer1;
        child2.network.outputLayer = parent1.network.outputLayer;

        return new ChildPair(child1, child2);
    }

    private WeightMapPair getWeightMaps(WeightMap parent1, WeightMap parent2) {
        WeightsPair weights = getWeights(parent1, parent2);
        BiasPair biases = getBiases(parent1, parent2);

        WeightMap first = new WeightMap(weights.first, biases.first);
        WeightMap second = new WeightMap(weights.second, biases.second);

        return new WeightMapPair(first, second);
    }

    private BiasPair getBiases(WeightMap parent1, WeightMap parent2) {
        double[] biasesOfFirst = new double[parent1.length];
        double[] biasesOfSecond = new double[parent1.length];

        boolean shouldCrossover = false;

        for (int i = 0; i < parent1.biasesLength; i++) {
            DoublePair doubles = getDoubles(parent1.getBias(i), parent2.getBias(i), shouldCrossover);
            biasesOfFirst[i] = doubles.first;
            biasesOfSecond[i] = doubles.second;
            shouldCrossover = shouldSwitch(shouldCrossover);
        }

        return new BiasPair(biasesOfFirst, biasesOfSecond);
    }

    private WeightsPair getWeights(WeightMap parent1, WeightMap parent2) {
        double[][] weightsOfFirst = new double[parent1.length][parent1.getWeights(0).length];
        double[][] weightsOfSecond = new double[parent1.length][parent1.getWeights(0).length];

        boolean shouldCrossover = false;

        for (int i = 0; i < parent1.length; i++) {
            for (int j = 0; j < parent1.getWeights(i).length; j++) {
                DoublePair doubles = getDoubles(parent1.getWeights(i)[j], parent2.getWeights(i)[j], shouldCrossover);
                weightsOfFirst[i][j] = doubles.first;
                weightsOfSecond[i][j] = doubles.second;
                shouldCrossover = shouldSwitch(shouldCrossover);
            }
        }

        return new WeightsPair(weightsOfFirst, weightsOfSecond);
    }

    private DoublePair getDoubles(double parent1, double parent2, boolean shouldCrossover) {
        double first;
        double second;

        if(Mutator.shouldMutate()){
            parent1 = Mutator.mutate(parent1);
        }
        if(Mutator.shouldMutate()){
            parent2 = Mutator.mutate(parent2);
        }

        if (shouldCrossover) {
            second = parent1;
            first = parent2;
        } else {
            first = parent1;
            second = parent2;
        }

        return new DoublePair(first, second);
    }

    private boolean shouldSwitch(boolean shouldCrossover) {
        if(random.nextDouble() > 0.99){
            return !shouldCrossover;
        }

        return shouldCrossover;
    }
}
