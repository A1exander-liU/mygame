package com.mygdx.game.engine.components.inventory.items.individual;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CharAttributes;

public class AffixesComponent implements Component {
    public Array<CharAttributes> affixes;

    public AffixesComponent() {}
}
