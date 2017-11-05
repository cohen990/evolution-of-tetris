package com.cohen990.ArtificialIntelligence;

import com.cohen990.Commands.Command;
import com.cohen990.Tetris;

public abstract class Strategy {
    public abstract Command pickMove(Tetris game);
}
