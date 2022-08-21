package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.InventoryChangeListener;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.AcceptedEquipType;
import com.mygdx.game.engine.Mappers;

public class InventorySlot extends ImageButton {
    // the item in this slot
    // this will be the full info
    Entity occupiedItem; // this is the slot (goes inside InventorySlot)
    boolean isEquipSlot = false;
    AcceptedEquipType acceptedEquipType;

    public InventorySlot(Skin skin) {
        super(skin);
    }

    public InventorySlot(Skin skin, AcceptedEquipType acceptedEquipType) {
        super(skin);
        isEquipSlot = true;
        this.acceptedEquipType = acceptedEquipType;
    }

    public Entity getOccupiedItem() {
        return this.occupiedItem;
    }

    public void setOccupiedItem(Entity occupiedItem) {
        this.occupiedItem = occupiedItem;
    }

    public AcceptedEquipType getAcceptedEquipType() {
        return acceptedEquipType;
    }

    public void swap(InventorySlot inventorySlot) {
        // swap the items that are occupying the slot
        // will be used with drag and drop
        Entity thisItem = occupiedItem;
        this.occupiedItem = inventorySlot.occupiedItem;
        inventorySlot.occupiedItem = thisItem;
    }

    public void stack(InventorySlot inventorySlot) {
        // if two items are similar stack them together
        Mappers.quantity.get(occupiedItem).quantity += Mappers.quantity.get(inventorySlot.occupiedItem).quantity;
        // since items are the same, the entity is no longer needed
        MyGame.engine.removeEntity(inventorySlot.occupiedItem);
        inventorySlot.occupiedItem = null;
    }

    public boolean isEmpty() {
        return  occupiedItem == null;
    }
}
