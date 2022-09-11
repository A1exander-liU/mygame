package com.mygdx.game.utils.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;

import java.util.Objects;

public class StartSaveButton extends TextButton {
    // will be the button to click to start the game
    // will either be continue or new depending if the slot has save data
    public StartSaveButton(String text, Skin skin, MyGame root, String slot) {
        super(text, skin);

        if (Objects.equals(root.getSaveStates().getSlotSerializedData(slot), "")) {
            setLabel(new Label("New", skin, "pixel2D", Color.BLACK));
        }
        else {
            setLabel(new Label("Continue", skin, "pixel2D", Color.BLACK));
        }



        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }
}
