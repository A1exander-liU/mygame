package com.mygdx.game.engine.components.inventory.items.shared;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.Type;

public class ItemTypeComponent implements Component {
    public Type itemType;

    public ItemTypeComponent() {}

    public ItemTypeComponent(Type type) {
        itemType = type;
    }
}
