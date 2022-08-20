package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.utils.InventorySlot;

public class InventoryUi {
    // will contain methods to manage the inventory
    // moving inventory items around

    Entity player;

    public InventoryUi() {
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    public void swapSlots(InventorySlot slot1, InventorySlot slot2) {
        // for dragging and dropping an item that is different
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        // get the indexes of each slot and reassign to other slot
        inventorySlots.set(inventorySlots.indexOf(slot1, true), slot2);
        inventorySlots.set(inventorySlots.indexOf(slot2, true), slot1);
    }
}
