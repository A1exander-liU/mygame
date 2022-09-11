package com.mygdx.game.utils.ui.saving;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MyGame;

import org.w3c.dom.Text;

public class DeleteConfirmDialog extends Dialog {
    MyGame root;
    String saveSlot;
    DeleteSaveButton deleteSaveButton;

    public DeleteConfirmDialog(String title, Skin skin, MyGame root, String saveSlot, DeleteSaveButton deleteSaveButton) {
        super(title, skin);
        getTitleLabel().setText("Do you want to delete this save file?");
        this.root = root;
        this.saveSlot = saveSlot;
        this.deleteSaveButton = deleteSaveButton;
        buildDialog();
    }

    @Override
    protected void result(Object result) {
        if (result == "yes") {
            // remove the slot data by setting to an empty string
            root.getSaveStates().setSlotData(saveSlot, "");
            updateLoadDataButton();
        }
    }

    private void buildDialog() {
        button("No", "no");
        button("Yes", "yes");
    }

    private void updateLoadDataButton() {
        Table saveFile = (Table) deleteSaveButton.getParent();
        Actor actor = new StartSaveButton("", getSkin(), root, saveSlot);
        Cell<?> cell = saveFile.getCells().get(2);
        cell.setActor(actor);
    }
}
