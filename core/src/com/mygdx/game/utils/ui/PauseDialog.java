package com.mygdx.game.utils.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

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
        TextButton closeButton = new TextButton("x", getSkin());
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
        getTitleLabel().setText("Paused");
        getTitleLabel().setAlignment(Align.center);
        getTitleTable().add(closeButton).expand().right().pad(10,10,0,0);
        getButtonTable().defaults().expand().fill().pad(5);
        button("Main Menu", "mainMenu");
        getButtonTable().row();
        button("Save", "save");
    }
}
