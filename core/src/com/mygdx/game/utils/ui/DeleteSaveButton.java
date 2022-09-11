package com.mygdx.game.utils.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class DeleteSaveButton extends TextButton {

    public DeleteSaveButton(String text, Skin skin) {
        super(text, skin);
        setLabel(new Label("x", skin, "pixel2D", Color.BLACK));
    }
}
