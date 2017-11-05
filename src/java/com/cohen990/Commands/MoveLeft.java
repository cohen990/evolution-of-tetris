package com.cohen990.Commands;

import com.cohen990.Tetris;

public class MoveLeft extends Command {
    public MoveLeft(Tetris game) {
        super(game);
    }

    @Override
    public void Execute() {
        game.move(-1);
    }
}
