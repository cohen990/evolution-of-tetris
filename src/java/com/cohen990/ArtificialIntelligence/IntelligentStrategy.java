package com.cohen990.ArtificialIntelligence;

import com.cohen990.*;
import com.cohen990.Commands.Command;

import java.awt.*;
import java.util.Random;

public class IntelligentStrategy extends Strategy {

    private final int numberOfDistinctTetraminos = 7;
    private final TetrisPlayer player;
    private int widthOfWell;
    private int heightOfWell;
    private int sizeOfWell;
    private int sizeOfInputLayer;
    private int numberOfDistinctCommands;

    public IntelligentStrategy(Tetris game, TetrisPlayer player){
        this.player = player;
        initializeNeuralNetwork(game, player);
    }

    private void initializeNeuralNetwork(Tetris game, TetrisPlayer player) {
        Random random = new Random();
        int sizeOfHiddenLayer = 1000;
        numberOfDistinctCommands = 7;
        int sizeOfOutputLayer = numberOfDistinctCommands;

        if(player == null)
            player = new TetrisPlayer();

        widthOfWell = game.well.length - 2;
        heightOfWell = game.well[0].length - 2;
        sizeOfWell = widthOfWell * heightOfWell;
        sizeOfInputLayer = sizeOfWell + 13;

        ensurePlayerHasNetwork(player, random, sizeOfHiddenLayer, sizeOfOutputLayer);

        Node[] inputLayer = new Node[sizeOfInputLayer];
        Node[] hiddenLayer = new Node[sizeOfHiddenLayer];
        Node[] outputLayer = new Node[numberOfDistinctCommands];

        for(int i = 0; i < inputLayer.length; i++){
            inputLayer[i] = new Node("input");
        }

        player.network.inputLayer = new Layer(inputLayer);
        player.network.hiddenLayer1 = new Layer(hiddenLayer);
        player.network.outputLayer = new Layer(outputLayer);
    }

    private void ensurePlayerHasNetwork(TetrisPlayer player, Random random, int sizeOfHiddenLayer, int sizeOfOutputLayer) {
        if(player.network == null) {
            Network network = new Network();

            double[][] inputToHiddenWeights = getEmptyWeights(random, sizeOfInputLayer, sizeOfHiddenLayer);
            double[][] hiddenToOutputWeights = getEmptyWeights(random, sizeOfHiddenLayer, sizeOfOutputLayer);

            double[] hiddenBiases = getEmptyBiases(random, sizeOfHiddenLayer);
            double[] outputBiases = getEmptyBiases(random, sizeOfOutputLayer);

            network.inputToHidden1 = new WeightMap(inputToHiddenWeights, hiddenBiases);
            network.hidden2ToOutput = new WeightMap(hiddenToOutputWeights, outputBiases);

            player.network = network;
        }
    }

    private double[] getEmptyBiases(Random random, int sizeOfLayer) {
        double[] biases = new double[sizeOfLayer];
        for(int i = 0; i < biases.length; i++){
            biases[i] = random.nextGaussian()/10000;
        }
        return biases;
    }

    private double[][] getEmptyWeights(Random random, int sizeOfFromLayer, int sizeOfToLayer) {
        double[][] weights = new double[sizeOfToLayer][sizeOfFromLayer];
        for(int i = 0; i < weights.length; i++){
            for(int j = 0; j < weights[i].length; j++){
                weights[i][j] = random.nextGaussian()/sizeOfFromLayer;
            }
        }
        return weights;
    }

    @Override
    public Command pickMove(Tetris game) {
        int tetraminoValue = game.Tetraminos[game.currentPiece].numericalValue;

        for(int i = 0; i < widthOfWell; i++){
            for(int j = 0; j < heightOfWell; j++){
                int currentNodeIndex = getCurrentNodeIndex(i, j, heightOfWell);
                if(isBlack(game.well[i+1][j])){
                    player.network.inputLayer.set(currentNodeIndex, new Node("well"));
                } else {
                    player.network.inputLayer.set(currentNodeIndex, new Node(1, "well"));
                }
            }
        }
        
        for(int i = sizeOfWell; i < numberOfDistinctTetraminos + sizeOfWell; i++){
            if(i == sizeOfWell + tetraminoValue){
                player.network.inputLayer.set(i, new Node(1, "tetramino"));
            } else{
                player.network.inputLayer.set(i, new Node("tetramino"));
            }
        }

        int indexOfXCoordinate = sizeOfInputLayer - 6;
        int indexOfYCoordinate = sizeOfInputLayer - 5;
        player.network.inputLayer.set(indexOfXCoordinate, new Node(game.pieceOrigin.x, "x-coordinate"));
        player.network.inputLayer.set(indexOfYCoordinate, new Node(game.pieceOrigin.y, "y-coordinate"));

        int startOfRotations = indexOfYCoordinate + 1;
        for(int i = startOfRotations; i < sizeOfInputLayer; i++){
            if(i == startOfRotations + game.rotation) {
                player.network.inputLayer.set(i, new Node(1, "rotation"));
            } else {
                player.network.inputLayer.set(i, new Node("rotation"));
            }
        }

//        for(int i = 0; i < inputLayer.length; i++){
//            System.out.printf("%d: %s\n", i, inputLayer[i]);
//        }

        Command bestMove = evaluateBestMove(game);

//        System.out.printf("Chosen %s!\n", bestMove.getClass().getSimpleName());

        return bestMove;
    }

    private Command evaluateBestMove(Tetris game) {
        return player.network.evaluate(game);
    }

    private int getCurrentNodeIndex(int i, int j, int sizeOfJ) {
        return (i * sizeOfJ) + j;
    }

    private boolean isBlack(Color color) {
        if(color.getBlue() == color.getRed() &&
            color.getRed() == color.getGreen() &&
            color.getGreen() == 0){
            return true;
        }

        return false;
    }
}
