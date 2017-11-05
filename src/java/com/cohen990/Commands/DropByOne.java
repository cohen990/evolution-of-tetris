package com.cohen990.Commands;

import com.cohen990.Tetris;

public class DropByOne extends Command {
    public DropByOne(Tetris game) {
        super(game);
    }

    @Override
    public void execute() {
        game.dropDown();
    }
}
