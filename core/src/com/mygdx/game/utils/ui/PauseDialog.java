package com.mygdx.game.utils.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseDialog extends Dialog {

    public PauseDialog(String title, Skin skin) {
        super(title, skin);
        buildPauseDialog();
    }

    @Override
    protected void result(Object result) {
        // hide to remove
    }

    private void buildPauseDialog() {
        getTitleLabel().setText("Paused");
        getTitleTable().add(new TextButton("x", getSkin())).expand().right();
        getButtonTable().defaults().expand().fill().pad(5);
        button("Main Menu", "mainMenu");
        getButtonTable().row();
        button("Save", "save");
    }
}
