package com.mygdx.game.utils.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGame;

public class DeleteConfirmDialog extends Dialog {
    MyGame root;
    String saveSlot;

    public DeleteConfirmDialog(String title, Skin skin, MyGame root, String saveSlot) {
        super(title, skin);
        getTitleLabel().setText("Do you want to delete this save file?");
        this.root = root;
        buildDialog();
    }

    @Override
    protected void result(Object result) {
        if (result == "yes") {
            // remove the slot data by setting to an empty string
            root.getSaveStates().setSlotData(saveSlot, "");
        }
    }

    private void buildDialog() {
        button("No", "no");
        button("Yes", "yes");
    }
}
