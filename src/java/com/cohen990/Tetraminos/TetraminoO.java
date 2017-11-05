package com.cohen990.Tetraminos;

import java.awt.*;

public class TetraminoO extends Tetramino{
    public TetraminoO(){
        super(new Point[][] {
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
        }, 3);
    }
}
