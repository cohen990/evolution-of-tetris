package com.cohen990.Reproduction;

import java.util.Random;

public class Mutator {
    public static boolean shouldMutate() {
        double random = new Random().nextDouble();
        return Math.abs(random) > 0.9;
    }

    public static double mutate(double weight) {
        double random = new Random().nextGaussian()/3;

        return weight + (weight * random);
    }
}
