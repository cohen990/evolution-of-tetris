package com.cohen990;

import com.cohen990.ArtificialIntelligence.RandomStrategy;
import com.cohen990.ArtificialIntelligence.Strategy;
import com.cohen990.Commands.*;
import com.cohen990.Tetraminos.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JPanel;

// stolen from https://gist.github.com/DataWraith/5236083
public class Tetris extends JPanel {
    private final Tetramino[] Tetraminos = Tetramino.initializeTetraminos();

    private final Color[] tetraminoColors = {
            Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
    };

    public Point pieceOrigin;
    private int currentPiece;
    public int rotation;
    private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

    private long score;
    private Color[][] well;

    private static JFrame GameFrame;

    private static final int GAME_CLOCK = 1000;
    private static final int AI_CLOCK = 1000;
    private static Thread gameThread;
    private static Thread aiThread;

    // Creates a border around the well and initializes the dropping piece
    private void init() {
        well = new Color[12][24];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                if (i == 0 || i == 11 || j == 22) {
                    well[i][j] = Color.GRAY;
                } else {
                    well[i][j] = Color.BLACK;
                }
            }
        }
        newPiece();
    }

    // Put a new, random piece into the dropping position
    public void newPiece() {
        pieceOrigin = new Point(5, 2);
        rotation = 0;
        if (nextPieces.isEmpty()) {
            Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(nextPieces);
        }
        currentPiece = nextPieces.get(0);

        if(collidesAt(pieceOrigin.x, pieceOrigin.y, rotation))
        {
            gameOver();
        }
        nextPieces.remove(0);
    }

    private void gameOver() {
        GameFrame.dispose();
        aiThread.stop();
        gameThread.stop();
    }

    // Collision test for the dropping piece
    public boolean collidesAt(int x, int y, int rotation) {
        for (Point p : currentTetraminoPoints()[rotation]) {
            if (well[p.x + x][p.y + y] != Color.BLACK) {
                return true;
            }
        }
        return false;
    }

    private Point[][] currentTetraminoPoints() {
        return Tetraminos[currentPiece].Points;
    }

    // Drops the piece one line or fixes it to the well if it can't drop
    public void dropToBottom() {
        while (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
            pieceOrigin.y += 1;
        }
        fixToWell();
        repaint();
    }

    // Make the dropping piece part of the well, so it is available for
    // collision detection.
    public void fixToWell() {
        for (Point p : currentTetraminoPoints()[rotation]) {
            well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
        }
        clearRows();
        newPiece();
    }

    public void deleteRow(int row) {
        for (int j = row-1; j > 0; j--) {
            for (int i = 1; i < 11; i++) {
                well[i][j+1] = well[i][j];
            }
        }
    }

    // Clear completed rows from the field and award score according to
    // the number of simultaneously cleared rows.
    public void clearRows() {
        boolean gap;
        int numClears = 0;

        for (int j = 21; j > 0; j--) {
            gap = false;
            for (int i = 1; i < 11; i++) {
                if (well[i][j] == Color.BLACK) {
                    gap = true;
                    break;
                }
            }
            if (!gap) {
                deleteRow(j);
                j += 1;
                numClears += 1;
            }
        }

        switch (numClears) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 500;
                break;
            case 4:
                score += 800;
                break;
        }
    }

    // Draw the falling piece
    private void drawPiece(Graphics g) {
        g.setColor(tetraminoColors[currentPiece]);
        for (Point p : currentTetraminoPoints()[rotation]) {
            g.fillRect((p.x + pieceOrigin.x) * 26,
                    (p.y + pieceOrigin.y) * 26,
                    25, 25);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // Paint the well
        g.fillRect(0, 0, 26*12, 26*23);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                g.setColor(well[i][j]);
                g.fillRect(26*i, 26*j, 25, 25);
            }
        }

        // Display the score
        g.setColor(Color.WHITE);
        g.drawString("" + score, 19*12, 25);

        // Draw the currently falling piece
        drawPiece(g);
    }

    public static void main(String[] args) {
        final Tetris game = new Tetris();
        game.init();

        initialiseGameFrame(game);

        Strategy aiStrategy = new RandomStrategy();

        aiThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(AI_CLOCK);
                    Command command = aiStrategy.pickMove(game);
                    command.execute();
                } catch ( InterruptedException e ) {}
            }
        });

        aiThread.start();

        // Make the falling piece drop every second
        gameThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(GAME_CLOCK);
                    new DropByOne(game).execute();
                } catch ( InterruptedException e ) {}
            }
        });

        gameThread.start();
    }

    private static void initialiseGameFrame(Tetris game) {
        GameFrame = new JFrame("Tetris");
        GameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameFrame.setSize(12*26+10, 26*23+25);
        GameFrame.setVisible(true);
        GameFrame.add(game);
    }
}