package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.inventory.gameitem.CharAttributes;

public class AffixesComponent implements Component {
    public Array<CharAttributes> affixes;

    public AffixesComponent() {}

    public AffixesComponent(Array<CharAttributes> affixes) {
        this.affixes = affixes;
    }
}
