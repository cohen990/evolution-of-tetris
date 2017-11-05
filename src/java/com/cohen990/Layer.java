package com.cohen990;

import com.cohen990.ArtificialIntelligence.Node;

public class Layer{
    private Node[] layer;
    public final int length;

    public Layer(Node[] layer) {
        this.layer = layer;
        length = layer.length;
    }

    public void set(int currentNodeIndex, Node value) {
        layer[currentNodeIndex] = value;
    }

    public Node get(int i) {
        return layer[i];
    }

    @Override
    public String toString() {
        String output = "";

        for(int i = 0; i < length; i++){
            output += layer[i].toString() + "\n";
        }

        return output;
    }
}
