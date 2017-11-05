package com.cohen990.Commands;

import com.cohen990.Tetris;

public class RotateClockwise extends Rotate {
    public RotateClockwise(Tetris game) {
        super(game, RotationDirection.clockwise);
    }
}
