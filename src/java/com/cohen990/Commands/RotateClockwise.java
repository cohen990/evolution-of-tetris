package com.cohen990.Commands;

import com.cohen990.Tetris;

public class RotateClockwise extends Command {
    public RotateClockwise(Tetris game) {
        super(game);
    }

    @Override
    public void Execute() {
        this.game.rotate(+1);
    }
}
