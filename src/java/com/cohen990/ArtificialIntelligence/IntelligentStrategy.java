package com.cohen990.ArtificialIntelligence;

import com.cohen990.*;
import com.cohen990.Commands.Command;
import com.cohen990.Commands.NullCommand;

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
        widthOfWell = game.well.length - 2;
        heightOfWell = game.well[0].length - 2;
        sizeOfWell = widthOfWell * heightOfWell;
        numberOfDistinctCommands = 7;
        sizeOfInputLayer = sizeOfWell + 13;
        int sizeOfHiddenLayer = 1000;
        int sizeOfOutputLayer = numberOfDistinctCommands;

        if(player == null)
            player = new TetrisPlayer();

        if(player.network == null) {
            Network network = new Network();
            double[][] inputToHiddenWeights = new double[sizeOfHiddenLayer][sizeOfInputLayer];
            double[][] hiddenToOutputWeights = new double[sizeOfOutputLayer][sizeOfHiddenLayer];

            for(int i = 0; i < inputToHiddenWeights.length; i++){
                for(int j = 0; j < inputToHiddenWeights[i].length; j++){
                    inputToHiddenWeights[i][j] = random.nextGaussian()/sizeOfInputLayer;
                }
            }

            for(int i = 0; i < hiddenToOutputWeights.length; i++){
                for(int j = 0; j < hiddenToOutputWeights[i].length; j++){
                    hiddenToOutputWeights[i][j] = random.nextGaussian()/sizeOfHiddenLayer;
                }
            }

            network.inputToHidden = new WeightMap(inputToHiddenWeights);
            network.hiddenToOutput = new WeightMap(hiddenToOutputWeights);

            player.network = network;
        }

        Node[] inputLayer = new Node[sizeOfInputLayer];
        Node[] hiddenLayer = new Node[sizeOfHiddenLayer];
        Node[] outputLayer = new Node[numberOfDistinctCommands];

        for(int i = 0; i < inputLayer.length; i++){
            inputLayer[i] = new Node("input");
        }

        player.network.inputLayer = new Layer(inputLayer);
        player.network.hiddenLayer = new Layer(hiddenLayer);
        player.network.outputLayer = new Layer(outputLayer);
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

        //System.out.printf("Chosen %s!\n", bestMove.getClass().getSimpleName());

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
