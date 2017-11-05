package com.cohen990.ArtificialIntelligence;

import com.cohen990.Commands.*;
import com.cohen990.Tetris;

public class RandomStrategy extends Strategy {
    @Override
    public Command pickMove(Tetris game) {

        java.util.Random random = new java.util.Random();

        Command[] possibleCommands = {
                new DropByOne(game),
                new DropToBottom(game),
                new MoveLeft(game),
                new MoveRight(game),
                new RotateCounterClockwise(game),
                new RotateClockwise(game),
                new NullCommand(game),
        };

        int randomSelection = random.nextInt(possibleCommands.length);
        Command selected = possibleCommands[randomSelection];

        System.out.printf("selected %s\n", selected.getClass().getSimpleName());

        return selected;
    }
}
