package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.utils.InventorySlot;

public class InventoryChangeListener extends ChangeListener {

    Stage stage;

    public InventoryChangeListener() {
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        InventorySlot inventorySlot = (InventorySlot) actor;
        // check if inventory slot holds an item
        if (!inventorySlot.isEmpty()) {
            System.out.println("has an item");
        }
    }
}
