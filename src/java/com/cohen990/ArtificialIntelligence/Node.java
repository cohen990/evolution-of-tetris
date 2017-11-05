package com.cohen990.ArtificialIntelligence;

public class Node {
    public double value;
    public String comment;

    public Node(String comment){
        value = 0;
        this.comment = comment;
    }

    public Node(int i, String comment) {
        value = i;
        this.comment = comment;
    }

    public Node(double i, String comment) {
        value = i;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return String.format("Value: %.2f - %s", value, comment);
    }
}
