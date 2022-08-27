package com.mygdx.game.engine.components.inventory.items.shared;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.engine.ItemType;

public class InventoryItemComponent implements Component {
    public ItemType acceptedItemType;

    public InventoryItemComponent() {}

    public InventoryItemComponent(ItemType acceptedItemType) {
        this.acceptedItemType = acceptedItemType;
    }
}
