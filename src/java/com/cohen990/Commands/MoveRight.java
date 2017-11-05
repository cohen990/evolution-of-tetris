package com.cohen990.Commands;

import com.cohen990.Tetris;

public class MoveRight extends Command {
    public MoveRight(Tetris game) {
        super(game);
    }

    @Override
    public void execute() {
        game.move(+1);
    }
}
