package com.mygdx.game.inventory.gameitem;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.inventory.gameitem.Rarity;

public class RarityColour {

    public RarityColour() {}

    public static Color getColour(Rarity rarity) {
        if (rarity == Rarity.COMMON)
            return Color.WHITE;
        else if (rarity == Rarity.UNCOMMON)
            return Color.LIME;
        else if (rarity == Rarity.RARE)
            return Color.valueOf("009CFFFF");
        else if (rarity == Rarity.EPIC)
            return Color.MAGENTA;
        else if (rarity == Rarity.LEGENDARY)
            return Color.CORAL;
        else if (rarity == Rarity.MYTHICAL)
            return Color.SCARLET;
        return Color.BLACK;
    }

}
