package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.utils.InventorySlot;

public class ItemSplitDialog extends Dialog {

    InventorySlot inventorySlot;
    Entity item;
    Entity player;
    InventoryUi inventoryUi;

    public ItemSplitDialog(String title, Skin skin, InventorySlot inventorySlot) {
        super(title, skin);
        this.inventorySlot = inventorySlot;
        item = inventorySlot.getOccupiedItem();
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        inventoryUi = new InventoryUi();
    }
}
