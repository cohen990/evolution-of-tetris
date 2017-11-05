package com.cohen990.Commands;

import com.cohen990.Tetris;

public class NullCommand extends Command {
    public NullCommand(Tetris game) {
        super(game);
    }

    @Override
    public void execute() {
        /* no op */
    }
}
