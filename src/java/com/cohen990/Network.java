package com.cohen990;

import com.cohen990.ArtificialIntelligence.Node;
import com.cohen990.Commands.*;

public class Network {
    public Layer inputLayer;
    public Layer hiddenLayer1;
    public Layer hiddenLayer2;
    public Layer outputLayer;

    public WeightMap inputToHidden1;
    public WeightMap hidden1ToHidden2;
    public WeightMap hidden2ToOutput;

    public Command evaluate(Tetris game) {
        evaluateFirstHiddenLayer();
        evaluateSecondHiddenLayer();
        evaluateOutputLayer();

        return getCommandFromOutputLayer(game);
    }

    private void evaluateFirstHiddenLayer() {
        evaluateLayer(inputLayer, hiddenLayer1, inputToHidden1);
    }

    private void evaluateSecondHiddenLayer() {
        evaluateLayer(hiddenLayer1, hiddenLayer2, hidden1ToHidden2);
    }

    private void evaluateOutputLayer() {
        evaluateLayer(hiddenLayer2, outputLayer, hidden2ToOutput);
    }

    private void evaluateLayer(Layer sourceLayer, Layer resultLayer, WeightMap weightsAndBiases){
        for(int i = 0; i < resultLayer.length; i++){
            Node node = evaluateNodeAgainstLayerWithWeightsAndBias(sourceLayer, weightsAndBiases.getWeights(i), weightsAndBiases.getBias(i), "");
            resultLayer.set(i, node);
        }
    }

    private Node evaluateNodeAgainstLayerWithWeightsAndBias(Layer layer, double[] weights, double bias, String comment) {
        float valueOfNode = 0;

        for(int i = 0; i < layer.length; i++){
            valueOfNode += (layer.get(i).value * weights[i]);
        }

        valueOfNode += bias;

        return new Node(sigmoid(valueOfNode), comment);
    }

    private double sigmoid(float valueOfNode) {
        return 1.0/(1.0 + Math.exp(-20 * valueOfNode));
    }

    public Command getCommandFromOutputLayer(Tetris game) {
        int indexOfGreatest = 0;
        double max = 0;

        for(int i = 0; i < outputLayer.length; i++){
            if(outputLayer.get(i).value > max){
                max = outputLayer.get(i).value;
                indexOfGreatest = i;
            }
        }

        switch(indexOfGreatest){
            case 1:
                return new DropByOne(game);
            case 2:
                return new DropToBottom(game);
            case 3:
                return new MoveLeft(game);
            case 4:
                return new MoveRight(game);
            case 5:
                return new RotateClockwise(game);
            case 6:
                return new RotateCounterClockwise(game);
            default:
                return new NullCommand(game);
        }
    }

    @Override
    public String toString() {
        return String.format("inputToHidden1:%s\nhidden2ToOutput:%s", inputToHidden1, hidden2ToOutput);
    }
}

