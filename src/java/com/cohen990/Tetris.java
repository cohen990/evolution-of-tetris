package com.cohen990;

import com.cohen990.ArtificialIntelligence.IntelligentStrategy;
import com.cohen990.ArtificialIntelligence.RandomStrategy;
import com.cohen990.ArtificialIntelligence.Strategy;
import com.cohen990.Commands.*;
import com.cohen990.Tetraminos.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.JPanel;

// stolen from https://gist.github.com/DataWraith/5236083
public class Tetris extends JPanel {
    public final Tetramino[] Tetraminos = Tetramino.initializeTetraminos();

    public final Color[] tetraminoColors = {
            Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
    };

    public final int gameHeight = 24;
    public final int gameWidth = 12;

    private final Painter painter = new Painter(this);
    private static final int GAME_CLOCK = 2;
    private static final int AI_CLOCK = GAME_CLOCK;

    public Point pieceOrigin;
    public int currentPiece;
    public int rotation;
    private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

    public long score;
    public Color[][] well;

    private static JFrame GameFrame;
    private static Thread gameThread;
    private static Thread aiThread;
    private static ExecutorService executorService;

    // Creates a border around the well and initializes the dropping piece
    private void init() {
        well = new Color[gameWidth][gameHeight];
        for (int i = 0; i < gameWidth; i++) {
            for (int j = 0; j < gameHeight-1; j++) {
                if (i == 0 || i == gameWidth - 1 || j == gameHeight - 2) {
                    well[i][j] = Color.GRAY;
                } else {
                    well[i][j] = Color.BLACK;
                }
            }
        }
        addNewPiece();
    }

    // Put a new, random piece into the dropping position
    public void addNewPiece() {
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
        executorService.shutdownNow();
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

    public Point[][] currentTetraminoPoints() {
        return Tetraminos[currentPiece].points;
    }

    // Make the dropping piece part of the well, so it is available for
    // collision detection.
    public void fixToWell() {
        for (Point p : currentTetraminoPoints()[rotation]) {
            well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
        }
        clearRows();
        addNewPiece();
    }

    public void deleteRow(int row) {
        for (int j = row-1; j > 0; j--) {
            for (int i = 1; i < gameWidth - 1; i++) {
                well[i][j+1] = well[i][j];
            }
        }
    }

    // Clear completed rows from the field and award score according to
    // the number of simultaneously cleared rows.
    public void clearRows() {
        boolean gap;
        int numClears = 0;

        for (int j = gameHeight - 3; j > 0; j--) {
            gap = false;
            for (int i = 1; i < gameWidth - 1; i++) {
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
                score += 1000;
                break;
            case 2:
                score += 3000;
                break;
            case 3:
                score += 5000;
                break;
            case 4:
                score += 8000;
                break;
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        painter.paintComponent(g);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int populationSize = 10;

        TetrisPlayer[] players = new TetrisPlayer[populationSize];

        boolean experimentIsNotComplete = true;

        Integer generation = 1;

        while(experimentIsNotComplete) {

            for (int i = 0; i < populationSize; i++) {
                final Tetris game = new Tetris();
                game.init();

                initialiseGameFrame(game);

                Strategy aiStrategy = new IntelligentStrategy(game);

                aiThread = new Thread(() -> {
                    boolean finished = false;
                    while (!finished) {
                        try {
                            Thread.sleep(AI_CLOCK);
                        } catch (InterruptedException e) {
                            finished = true;
                        }
                        Command command = aiStrategy.pickMove(game);
                        command.execute();
                    }
                });


                // Make the falling piece drop every second
                gameThread = new Thread(() -> {
                    boolean finished = false;
                    while (!finished) {
                        try {
                            Thread.sleep(GAME_CLOCK);
                            new DropByOne(game).execute();
                        } catch (InterruptedException e) {
                            finished = true;
                        }
                    }
                });

                executorService = Executors.newCachedThreadPool();

                executorService.execute(gameThread);
                executorService.execute(aiThread);

                executorService.awaitTermination(10, TimeUnit.SECONDS);

                System.out.println("finished");

                GameFrame.dispose();

                players[i] = new TetrisPlayer(((IntelligentStrategy) aiStrategy).network, game.score);
            }

            String directory = String.format("experiment1\\generation%d\\", generation);

            for(int i = 0; i < players.length; i++){
                writeResultToFile(directory, players[i]);
            }

            writeSummary(directory, players);

            generation = generation + 1;

            players = reproduce(players);
        }
    }

    private static TetrisPlayer[] reproduce(TetrisPlayer[] players) {
        Stream<TetrisPlayer> playersStream = Stream.of(players);

        playersStream = playersStream.sorted(Comparator.comparingLong(o -> o.score));

        playersStream = playersStream.skip(500).collect();

        TetrisPlayer[] children = new TetrisPlayer[500];



        return players;
    }

    private static void writeSummary(String pathName, TetrisPlayer[] players) throws IOException {
        String fileName = pathName + "summary.txt";
        FileWriter writer = new FileWriter(fileName);

        int maxHash = 0;
        long max = Long.MIN_VALUE;
        int minHash = 0;
        long min = Long.MAX_VALUE;
        long total = 0;

        for(int i = 0; i < players.length; i++){
            TetrisPlayer player = players[i];
            long score = player.score;
            if(score > max){
                max = score;
                maxHash = player.hashCode();
            }
            if(score < min){
                min = score;
                minHash = player.hashCode();
            }
            total += score;
        }

        float average = (float) total/ players.length;

        writer.write(String.format("Max: %d - player: %d\n", max, maxHash));
        writer.write(String.format("Min: %d - player: %d\n", min, minHash));
        writer.write(String.format("Average: %f", average));

        writer.close();
    }

    private static void writeResultToFile(String pathName, TetrisPlayer element) throws IOException {
        File directory = new File(pathName);

        makeDirectoryIfNotExists(directory);

        System.out.println(element);
        String fileName = String.format("%s%d.txt", pathName, element.hashCode());
        System.out.printf("using %s\n", fileName);
        FileWriter writer = new FileWriter(fileName);
        writer.write(element.toLongString());
        writer.close();
    }

    private static void makeDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            System.out.println("creating directory: " + directory.getName());
            boolean result = false;

            try{
                directory.mkdirs();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
    }

    private static void initialiseGameFrame(Tetris game) {
        GameFrame = new JFrame("Tetris");
        GameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameFrame.setSize(12*26+10, 26*23+25);
        GameFrame.setVisible(true);
        GameFrame.add(game);
    }
}