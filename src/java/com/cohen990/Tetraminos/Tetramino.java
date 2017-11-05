package com.cohen990.Tetraminos;

import java.awt.*;

public class Tetramino {
    public Point[][] Points;

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
