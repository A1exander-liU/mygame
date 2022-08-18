package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Mappers;

import java.util.Objects;

public class InventorySlot extends Button {
    // the item in this slot
    // this will be the full info
    Entity occupiedItem;

    public InventorySlot(Skin skin) {
        super(skin);
    }

    public Entity getOccupiedItem() {
        return this.occupiedItem;
    }

    public void swap(InventorySlot inventorySlot) {
        // swap the items that are occupying the slot
        // will be used with drag and drop
        Entity thisItem = occupiedItem;
        this.occupiedItem = inventorySlot.occupiedItem;
        inventorySlot.occupiedItem = thisItem;
    }

}
