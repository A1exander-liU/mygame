package com.mygdx.game;

import com.mygdx.game.engine.Rarity;

import java.util.Random;

public class AffixValueGenerator {
    private static final Random random = new Random();

    public AffixValueGenerator() {}

    public static int generate(int itemLevel, Rarity itemRarity) {
        // determine range of affix value
        // floor( ( level / 3 ) + level ) - (level * 3)
        int minValue = (int)Math.floor(((itemLevel / 3f) + itemLevel));
        int maxValue = itemLevel * 3;
        return 0;
    }
}
