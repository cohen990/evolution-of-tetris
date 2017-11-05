package com.cohen990.Commands;

import com.cohen990.Tetris;

public class RotateCounterClockwise extends Rotate {
    public RotateCounterClockwise(Tetris game) {
        super(game, RotationDirection.counterClockwise);
    }
}
