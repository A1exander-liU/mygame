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

    boolean clicked = false;

    // add boolean field to see if slot was clicked
    // have system to run after inventory render to display pop up
    // go through the slots and see which was clicked
    // find the one that is clicked:
    // set the field to false
    // then check item type
    // then display the formatted window

    public InventorySlot(Skin skin) {
        super(skin);
        addListener(new InventoryChangeListener());
    }

    public InventorySlot(Skin skin, AcceptedEquipType acceptedEquipType) {
        super(skin);
        isEquipSlot = true;
        this.acceptedEquipType = acceptedEquipType;
        addListener(new InventoryChangeListener());
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

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isEmpty() {
        return  occupiedItem == null;
    }
}
