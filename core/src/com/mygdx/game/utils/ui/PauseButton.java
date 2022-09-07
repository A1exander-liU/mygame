package com.mygdx.game.utils.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PauseButton extends ImageButton {

    public PauseButton(Skin skin) {
        super(skin, "image-button-pause");
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // open up a window to go back to main menu screen
                // and put save button here too

            }
        });
    }
}
