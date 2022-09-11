package com.mygdx.game.utils.ui.saving;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;
import com.mygdx.game.utils.ui.saving.DeleteConfirmDialog;

public class DeleteSaveButton extends TextButton {
    MyGame root;
    String saveSlot;

    public DeleteSaveButton(String text, final Skin skin, final MyGame root, final String saveSlot) {
        super(text, skin);
        this.root = root;
        this.saveSlot = saveSlot;
        setLabel(new Label("x", skin, "pixel2D", Color.BLACK));
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DeleteConfirmDialog deleteConfirmDialog = new DeleteConfirmDialog("", skin, root, saveSlot);
                deleteConfirmDialog.show(getStage());
            }
        });
    }
}
