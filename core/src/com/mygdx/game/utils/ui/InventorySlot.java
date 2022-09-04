package com.mygdx.game.utils.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InventoryChangeListener;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.AcceptedEquipType;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.ItemType;

public class InventorySlot extends ImageButton {
    // the item in this slot
    // this will be the full info
    Entity occupiedItem; // this is the slot (goes inside InventorySlot)
    boolean isEquipSlot = false;
    AcceptedEquipType acceptedEquipType;
    ItemType acceptedItemType;

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

    public InventorySlot(Skin skin, ItemType acceptedItemType) {
        super(skin);
        isEquipSlot = true;
        this.acceptedItemType = acceptedItemType;
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

    public ItemType getAcceptedType() {
        return acceptedItemType;
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

    public InventoryChangeListener getItemWindowListener() {
        for (int i = 0; i < getListeners().size; i++) {
            if (getListeners().get(i) instanceof InventoryChangeListener)
                return (InventoryChangeListener) getListeners().get(i);
        }
        return null;
    }

    public Stack getStackChild() {
        return (Stack) getChildren().get(0);
    }

    public Image getItemImage() {
        Entity player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;
        if (equipSlots.contains(this, false))
            return (Image) getChildren().get(0);
        else
            return (Image) getStackChild().getChildren().get(0);
    }

}
