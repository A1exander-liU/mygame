package com.mygdx.game.utils;

import java.util.Random;

public class RandomNumberGenerator {

    private static final Random random = new Random();

    public RandomNumberGenerator() {}

    public static float roll() {
        return random.nextFloat() * 100;
    }

    public static float roll(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static int roll(int max) {
        return random.nextInt(max);
    }

    public static int roll(int min, int max) {
        return (random.nextInt(max)) + min;
    }
}
