package com.mygdx.game.engine.components.inventory.items.shared;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.ItemType;

public class InventoryItemComponent implements Component {
    public ItemType itemType;

    public InventoryItemComponent() {}

    public InventoryItemComponent(ItemType itemType) {
        this.itemType = itemType;
    }
}
