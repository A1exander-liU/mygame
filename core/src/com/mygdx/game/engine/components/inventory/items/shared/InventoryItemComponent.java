package com.mygdx.game.engine.components.inventory.items.shared;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Type;

public class InventoryItemComponent implements Component {
    public Type acceptedType;

    public InventoryItemComponent() {}

    public InventoryItemComponent(Type acceptedType) {
        this.acceptedType = acceptedType;
    }
}
