package com.cohen990.Commands;

import com.cohen990.Tetris;

public class DropByOne extends Command {
    public DropByOne(Tetris game) {
        super(game);
    }

    @Override
    public void execute() {
        if (!game.collidesAt(game.pieceOrigin.x, game.pieceOrigin.y + 1, game.rotation)) {
            game.pieceOrigin.y += 1;
        } else {
            game.fixToWell();
        }
        game.repaint();
    }
}
