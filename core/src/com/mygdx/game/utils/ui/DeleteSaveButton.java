package com.mygdx.game.utils.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;

public class DeleteSaveButton extends TextButton {
    MyGame root;
    String saveSlot;

    public DeleteSaveButton(String text, Skin skin, MyGame root, String saveSlot) {
        super(text, skin);
        this.root = root;
        this.saveSlot = saveSlot;
        setLabel(new Label("x", skin, "pixel2D", Color.BLACK));
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }
}
