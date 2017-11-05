package com.cohen990.Commands;

import com.cohen990.Tetris;

public class DropToBottom extends Command {
    public DropToBottom(Tetris game) {
        super(game);
    }

    @Override
    public void execute() {
        game.dropToBottom();
    }
}
