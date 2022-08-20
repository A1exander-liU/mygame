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

    public void stackItems(InventorySlot stacker, InventorySlot stack) {
        // stacker value will be added to stack
        // then dereference stacker occupied item since it was
        // combined into the stack
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        // add the quantity
        Mappers.quantity.get(stack.getOccupiedItem()).quantity += Mappers.quantity.get(stacker.getOccupiedItem()).quantity;
        // dereference the stacker item
        inventorySlots.get(inventorySlots.indexOf(stacker, true)).setOccupiedItem(null);
    }

    public void setItem(InventorySlot source, InventorySlot target) {
        // target is where source will be set to then source will be
        // dereferenced
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        // set the item to the empty slot
        inventorySlots.get(inventorySlots.indexOf(target, true)).setOccupiedItem(source.getOccupiedItem());
        // dereference the source in its original slot
        inventorySlots.get(inventorySlots.indexOf(source, true)).setOccupiedItem(null);
    }
}
