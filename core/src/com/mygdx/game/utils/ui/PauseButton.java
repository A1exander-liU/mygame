package com.mygdx.game.utils.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;

public class PauseButton extends ImageButton {
    MyGame root;

    public PauseButton(Skin skin, MyGame root) {
        super(skin, "image-button-pause");
        this.root = root;
        addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                // open up a window to go back to main menu screen
                // and put save button here too
                System.out.println("clicked");
                showPauseMenu();
            }
        });
    }

    private void showPauseMenu() {
        PauseDialog pauseDialog = new PauseDialog("", getSkin(), root);
        pauseDialog.show(getStage());
    }
}
