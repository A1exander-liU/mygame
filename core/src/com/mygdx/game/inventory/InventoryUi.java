package com.mygdx.game.inventory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.ui.InventorySlot;

import java.util.Objects;

public class InventoryUi {
    // will contain methods to manage the inventory
    // moving inventory items around
    public InventoryUi() {}

    public void swapItems(InventorySlot slot1, InventorySlot slot2) {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        // for dragging and dropping an item that is different
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        // store temp value to assign later
        Entity tempSlot1Item = slot1.getOccupiedItem();
        // set item in slot1 to item in slot2
        inventorySlots.get(inventorySlots.indexOf(slot1, true)).setOccupiedItem(slot2.getOccupiedItem());
        // set item in slot2 to item in slot1
        inventorySlots.get(inventorySlots.indexOf(slot2, true)).setOccupiedItem(tempSlot1Item);
    }

    public void stackItems(InventorySlot stacker, InventorySlot stack) {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        // stacker value will be added to stack
        // then dereference stacker occupied item since it was
        // combined into the stack
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;

        // check if stack is already at max quantity
        if (Mappers.quantity.get(stack.getOccupiedItem()).quantity == Mappers.stackable.get(stack.getOccupiedItem()).stackSize) {
            swapItems(stacker, stack);
        }
        // if item quantity is not at max
        else {
            // add the quantity
            Mappers.quantity.get(stack.getOccupiedItem()).quantity += Mappers.quantity.get(stacker.getOccupiedItem()).quantity;
            // check if stacking whet over stackSize of the item
            if (Mappers.quantity.get(stack.getOccupiedItem()).quantity > Mappers.stackable.get(stack.getOccupiedItem()).stackSize) {
                // get extra amount
                int extra = Mappers.quantity.get(stack.getOccupiedItem()).quantity - Mappers.stackable.get(stack.getOccupiedItem()).stackSize;
                // since stack went over stackSize, set it to it's stackSize
                Mappers.quantity.get(stack.getOccupiedItem()).quantity = Mappers.stackable.get(stack.getOccupiedItem()).stackSize;
                // extra becomes stacker's new quantity
                Mappers.quantity.get(stacker.getOccupiedItem()).quantity = extra;
            }
            else
                // dereference the stacker item
                inventorySlots.get(inventorySlots.indexOf(stacker, true)).setOccupiedItem(null);
        }
    }

    public void setItem(InventorySlot source, InventorySlot target) {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        // target is where source will be set to then source will be
        // dereferenced
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        // set the item to the empty slot
        inventorySlots.get(inventorySlots.indexOf(target, true)).setOccupiedItem(source.getOccupiedItem());
        // dereference the source in its original slot
        inventorySlots.get(inventorySlots.indexOf(source, true)).setOccupiedItem(null);
    }

    public void splitItem(InventorySlot source, InventorySlot target, int amount) {
        // do nothing since no item was split over
        if (amount == 0) return;

        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;

        // get leftover amount after split and set it as quantity of source item
        int leftover = Mappers.quantity.get(source.getOccupiedItem()).quantity - amount;
        
        Entity item = GameScreen.itemFactory.makeMaterial(Mappers.name.get(source.getOccupiedItem()).name, amount);

        // full stack was split over, so source is empty now
        if (amount == Mappers.quantity.get(source.getOccupiedItem()).quantity) {
            inventorySlots.get(inventorySlots.indexOf(source, true)).setOccupiedItem(null);
        }
        // the split amount was between 0 and sourceItem quantity (there is still leftover)
        else {
            Mappers.quantity.get(source.getOccupiedItem()).quantity = leftover;
            inventorySlots.get(inventorySlots.indexOf(source, true)).setOccupiedItem(source.getOccupiedItem());
        }

        // add new item with split quantity and place in target slot
        inventorySlots.get(inventorySlots.indexOf(target, true)).setOccupiedItem(item);
    }

    public void equip(InventorySlot source, InventorySlot target) {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        // equipment and inventory slots are separate arrays
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;
        // equip the item
        equipSlots.get(equipSlots.indexOf(target, true)).setOccupiedItem(source.getOccupiedItem());
        // dereference the equipment in the inventory
        inventorySlots.get(inventorySlots.indexOf(source, true)).setOccupiedItem(null);
    }

    public void swapEquipment(InventorySlot source, InventorySlot target) {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);

        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;

        // store source item since the occupiedItem will be changed to target occupiedItem
        Entity temp = source.getOccupiedItem();
        // move the equipped item to the source slot
        inventorySlots.get(inventorySlots.indexOf(source, true)).setOccupiedItem(target.getOccupiedItem());
        // move the source item to the equip slot
        equipSlots.get(equipSlots.indexOf(target, true)).setOccupiedItem(temp);
    }

    public void unequip(InventorySlot source, InventorySlot target) {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);

        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;
        // put equipped item back inventory
        inventorySlots.get(inventorySlots.indexOf(target, true)).setOccupiedItem(source.getOccupiedItem());
        // dereference equipped item in equipped slot
        equipSlots.get(equipSlots.indexOf(source, true)).setOccupiedItem(null);
    }

    public boolean addToInventory(Entity item) {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        // is item stackable
        if (Mappers.stackable.get(item) != null) {
            int slot = placeStackable(item, inventorySlots);
            return slot != -1;
        }
        // not stackable, just add to first empty slot
        else {
            int slot = addToFirstEmptySlot(item, inventorySlots);
            return slot != -1;
        }
    }

    private int placeStackable(Entity item, Array<InventorySlot> inventorySlots) {
        int slotAddedTo = addToFirstEmptySlot(item, inventorySlots);
        for (int i = 0; i < inventorySlots.size; i++) {
            if (inventorySlots.get(i).isEmpty()) continue;
            Entity inventoryItem = inventorySlots.get(i).getOccupiedItem();
            if ((i != slotAddedTo && slotAddedTo != -1) && Objects.equals(Mappers.name.get(inventoryItem).name, Mappers.name.get(item).name)) {
                stackItems(inventorySlots.get(slotAddedTo), inventorySlots.get(i));
            }
        }
        return slotAddedTo;
    }

    private int addToFirstEmptySlot(Entity item, Array<InventorySlot> inventorySlots) {
        for (int i = 0; i < inventorySlots.size; i++) {
            if (inventorySlots.get(i).isEmpty()) {
                inventorySlots.get(i).setOccupiedItem(item);
                return i;
            }
        }
        return -1;
    }

}