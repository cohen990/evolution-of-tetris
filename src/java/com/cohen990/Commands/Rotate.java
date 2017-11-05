package com.cohen990.Commands;

import com.cohen990.Tetris;

public class Rotate extends Command {
    private final int rotation;

    protected Rotate(Tetris game, RotationDirection rotationDirection) {
        super(game);
        if(rotationDirection == RotationDirection.clockwise) {
            rotation = 1;
        } else {
            rotation = -1;
        }
    }

    @Override
    public final void execute() {
        int newRotation = (game.rotation + rotation) % 4;
        if (newRotation < 0) {
            newRotation = 3;
        }

        if (!game.collidesAt(game.pieceOrigin.x, game.pieceOrigin.y, newRotation)) {
            game.rotation = newRotation;
        }

        game.repaint();
    }
}
