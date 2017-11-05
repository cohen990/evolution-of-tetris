package com.cohen990.Tetraminos;

import java.awt.*;

public class Tetramino {
    public Point[][] points;
    public int numericalValue;

    protected Tetramino(Point[][] points, int numericalValue){
        this.points = points;
        this.numericalValue = numericalValue;
    }

    public static Tetramino[] initializeTetraminos() {
        return new Tetramino[]{
            new TetraminoI(),
            new TetraminoJ(),
            new TetraminoL(),
            new TetraminoO(),
            new TetraminoS(),
            new TetraminoT(),
            new TetraminoZ(),
        };
    }
}
