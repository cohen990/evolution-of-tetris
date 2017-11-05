package com.cohen990.Commands;

import com.cohen990.Tetris;

public class Move extends Command {
    private final int direction;

    protected Move(Tetris game, MoveDirection moveDirection) {
        super(game);

        if(moveDirection == MoveDirection.right){
            direction = +1;
        } else {
            direction = -1;
        }
    }

    @Override
    public void execute() {
        if (!game.collidesAt(game.pieceOrigin.x + direction, game.pieceOrigin.y, game.rotation)) {
            game.pieceOrigin.x += direction;
        }
        game.repaint();
    }
}
