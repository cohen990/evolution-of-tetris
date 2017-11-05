package com.cohen990.Commands;

import com.cohen990.Tetris;

public class MoveLeft extends Move {
    public MoveLeft(Tetris game) {
        super(game, MoveDirection.left);
    }
}
