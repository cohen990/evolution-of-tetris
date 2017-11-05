package com.cohen990.Commands;

import com.cohen990.Tetris;

public abstract class Command {
    protected final Tetris game;

    public Command(Tetris game) {
        this.game = game;
    }

    public abstract void execute();
}
