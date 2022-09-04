package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.utils.gameitem.Rarity;

public class RarityComponent implements Component {
    public Rarity rarity;

    public RarityComponent() {}

    public RarityComponent(Rarity rarity) {
        this.rarity = rarity;
    }
}
