package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class InventoryChangeListener extends ChangeListener {

    Stage stage;

    public InventoryChangeListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {

    }
}
