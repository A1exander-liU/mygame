package com.mygdx.game.utils.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGame;

public class DeleteConfirmDialog extends Dialog {
    MyGame root;

    public DeleteConfirmDialog(String title, Skin skin, MyGame root) {
        super(title, skin);
        this.root = root;
        buildDialog();
    }

    @Override
    protected void result(Object result) {

    }

    private void buildDialog() {

    }
}
