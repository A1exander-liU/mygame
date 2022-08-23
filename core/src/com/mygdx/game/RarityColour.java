package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.engine.Rarity;

public class RarityColour {

    public RarityColour() {}

    public static Color getColour(Rarity rarity) {
        if (rarity == Rarity.COMMON)
            return Color.WHITE;
        else if (rarity == Rarity.UNCOMMON)
            return Color.LIME;
        else if (rarity == Rarity.RARE)
            return Color.NAVY;
        else if (rarity == Rarity.EPIC)
            return Color.PURPLE;
        else if (rarity == Rarity.LEGENDARY)
            return Color.ORANGE;
        else if (rarity == Rarity.MYTHICAL)
            return Color.FIREBRICK;
        return Color.BLACK;
    }

}
