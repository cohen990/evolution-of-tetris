package com.cohen990.Commands;

import com.cohen990.Tetris;

public class DropToBottom extends Command {
    public DropToBottom(Tetris game) {
        super(game);
    }

    @Override
    public void execute() {
        while (!game.collidesAt(game.pieceOrigin.x, game.pieceOrigin.y + 1, game.rotation)) {
            game.pieceOrigin.y += 1;
        }
        game.fixToWell();
        game.repaint();
    }
}
