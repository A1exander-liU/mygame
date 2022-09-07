package com.mygdx.game.utils.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PauseDialog extends Dialog {

    public PauseDialog(String title, Skin skin) {
        super(title, skin);
    }

    @Override
    protected void result(Object result) {
        // hide to remove
    }
}
