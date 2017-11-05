package com.cohen990.Commands;

import com.cohen990.Tetris;

public class RotateCounterClockwise extends Command {
    public RotateCounterClockwise(Tetris game) {
        super(game);
    }

    @Override
    public void execute() {
        game.rotate(-1);
    }
}
