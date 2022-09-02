package com.mygdx.game;

import java.util.Random;

public class RandomNumberGenerator {

    private static final Random random = new Random();

    public RandomNumberGenerator() {}

    public static float roll() {
        return random.nextFloat() * 100;
    }

    public static float roll(int min, int max) {
        return (random.nextFloat() * max) + min;
    }

    public static int roll(int max) {
        return random.nextInt(max);
    }
}
