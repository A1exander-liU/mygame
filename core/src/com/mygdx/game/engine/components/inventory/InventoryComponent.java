package com.mygdx.game.engine.components.inventory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;

public class InventoryComponent implements Component {
    // how many unique items can be held
    public int capacity = 20;
    public Array<Entity> items = new Array<>(capacity);
    public boolean opened = false;
    // each item will contain these components:
    // ALL: all items must have, ONE: have one of the listed
    // ALL:
    // InventoryItemComponent:
    // NameComponent:
    // RarityComponent:
    // DescriptionComponent:
    // SpriteComponent:

    // ONE:
    // EquipmentComponent: Will also have enum to determine the type
    // ConsumableComponent:
    // MaterialComponent:

    // StackableComponent: a flag to determine item is stackable
    // QuantityComponent: for items where multiple can be obtained at once
    // like getting 3 wood for chopping a tree

    public InventoryComponent() {
        for (int i = 0; i < capacity; i++) {
            Entity slot = new Entity();
            slot.add(new InventorySlotComponent());
            items.add(slot);
            MyGame.engine.addEntity(slot);
        }
    }

}
