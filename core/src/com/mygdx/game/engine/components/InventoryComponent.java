package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

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

    // ONE:
    // EquipmentComponent: Will also have enum to determine the type
    // ConsumableComponent:
    // MaterialComponent:

    // StackableComponent: a flag to determine item is stackable
    // QuantityComponent: for items where multiple can be obtained at once
    // like getting 3 wood for chopping a tree


}
