package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.utils.InventorySlot;

public class InventoryChangeListener extends ChangeListener {

    Stage stage;

    public InventoryChangeListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        InventorySlot inventorySlot = (InventorySlot) actor;
        // check if inventory slot holds an item
        if (!inventorySlot.isEmpty()) {

        }
    }
}
