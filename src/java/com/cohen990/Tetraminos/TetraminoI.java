package com.cohen990.Tetraminos;

import java.awt.*;

public class TetraminoI extends Tetramino{
    public TetraminoI(){
        Points = new Point[][] {
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
        };
    }
}
