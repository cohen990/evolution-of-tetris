package com.cohen990;

import java.awt.*;

public class Painter {
    private final Tetris game;

    public Painter(Tetris game){
        this.game = game;
    }

    private void drawPiece(Graphics g) {
        g.setColor(game.tetraminoColors[game.currentPiece]);
        for (Point p : game.currentTetraminoPoints()[game.rotation]) {
            g.fillRect((p.x + game.pieceOrigin.x) * 26,
                    (p.y + game.pieceOrigin.y) * 26,
                    25, 25);
        }
    }

    public void paintComponent(Graphics g){
        // Paint the well
        g.fillRect(0, 0, 26*12, 26*23);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                g.setColor(game.well[i][j]);
                g.fillRect(26*i, 26*j, 25, 25);
            }
        }

        // Display the score
        g.setColor(Color.WHITE);
        g.drawString("" + game.playerIndex, 19*12, 25);

        // Draw the currently falling piece
        drawPiece(g);
    }
}
