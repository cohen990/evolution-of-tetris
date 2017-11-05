package com.cohen990.ArtificialIntelligence;

import com.cohen990.Commands.Command;
import com.cohen990.Commands.NullCommand;
import com.cohen990.Layer;
import com.cohen990.Network;
import com.cohen990.Tetris;
import com.cohen990.WeightMap;

import java.awt.*;
import java.util.Random;

public class IntelligentStrategy extends Strategy {

    private final int numberOfDistinctTetraminos = 7;
    private int widthOfWell;
    private int heightOfWell;
    private int sizeOfWell;
    private int sizeOfInputLayer;
    private int numberOfDistinctCommands;

    public Network network;


    private Node[] hiddenLayer;
    private Node[] outputLayer;

    private double[][] inputToHiddenWeights;
    private double[][] hiddenToOutputWeights;

    public IntelligentStrategy(Tetris game){
        initializeNeuralNetwork(game);
    }

    private void initializeNeuralNetwork(Tetris game) {
        network = new Network();
        widthOfWell = game.well.length - 2;
        heightOfWell = game.well[0].length - 2;
        sizeOfWell = widthOfWell * heightOfWell;
        sizeOfInputLayer = sizeOfWell + 13;
        numberOfDistinctCommands = 7;

        Node[] inputLayer = new Node[sizeOfInputLayer];
        hiddenLayer = new Node[10];
        outputLayer = new Node[numberOfDistinctCommands];

        inputToHiddenWeights = new double[hiddenLayer.length][inputLayer.length];
        hiddenToOutputWeights = new double[outputLayer.length][hiddenLayer.length];

        Random random = new Random();

        for(int i = 0; i < inputLayer.length; i++){
            inputLayer[i] = new Node("input");
        }

        for(int i = 0; i < inputToHiddenWeights.length; i++){
            for(int j = 0; j < inputToHiddenWeights[i].length; j++){
                inputToHiddenWeights[i][j] = random.nextGaussian()/1000;
            }
        }

        for(int i = 0; i < hiddenToOutputWeights.length; i++){
            for(int j = 0; j < hiddenToOutputWeights[i].length; j++){
                hiddenToOutputWeights[i][j] = random.nextGaussian()/1000;
            }
        }

        network.inputLayer = new Layer(inputLayer);
        network.hiddenLayer = new Layer(hiddenLayer);
        network.outputLayer = new Layer(outputLayer);

        network.inputToHidden = new WeightMap(inputToHiddenWeights);
        network.hiddenToOutput = new WeightMap(hiddenToOutputWeights);
    }

    @Override
    public Command pickMove(Tetris game) {
        int tetraminoValue = game.Tetraminos[game.currentPiece].numericalValue;

        for(int i = 0; i < widthOfWell; i++){
            for(int j = 0; j < heightOfWell; j++){
                int currentNodeIndex = getCurrentNodeIndex(i, j, heightOfWell);
                if(isBlack(game.well[i+1][j])){
                    network.inputLayer.set(currentNodeIndex, new Node("well"));
                } else {
                    network.inputLayer.set(currentNodeIndex, new Node(1, "well"));
                }
            }
        }
        
        for(int i = sizeOfWell; i < numberOfDistinctTetraminos + sizeOfWell; i++){
            if(i == sizeOfWell + tetraminoValue){
                network.inputLayer.set(i, new Node(1, "tetramino"));
            } else{
                network.inputLayer.set(i, new Node("tetramino"));
            }
        }

        int indexOfXCoordinate = sizeOfInputLayer - 6;
        int indexOfYCoordinate = sizeOfInputLayer - 5;
        network.inputLayer.set(indexOfXCoordinate, new Node(game.pieceOrigin.x, "x-coordinate"));
        network.inputLayer.set(indexOfYCoordinate, new Node(game.pieceOrigin.y, "y-coordinate"));

        int startOfRotations = indexOfYCoordinate + 1;
        for(int i = startOfRotations; i < sizeOfInputLayer; i++){
            if(i == startOfRotations + game.rotation) {
                network.inputLayer.set(i, new Node(1, "rotation"));
            } else {
                network.inputLayer.set(i, new Node("rotation"));
            }
        }

//        for(int i = 0; i < inputLayer.length; i++){
//            System.out.printf("%d: %s\n", i, inputLayer[i]);
//        }

        Command bestMove = evaluateBestMove(game);

        System.out.printf("Chosen %s!\n", bestMove.getClass().getSimpleName());

        return bestMove;
    }

    private Command evaluateBestMove(Tetris game) {
        return network.evaluate(game);
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
