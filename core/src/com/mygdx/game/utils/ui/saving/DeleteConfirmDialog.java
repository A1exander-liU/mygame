package com.mygdx.game.utils.ui.saving;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
        getTitleLabel().setName("title");
        getTitleTable().removeActor(getTitleTable().findActor("title"));
        getTitleTable().add(new Label("Do you want to delete this save file?", skin, "pixel2D", Color.BLACK));
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
        getButtonTable().defaults().pad(5);
        button("No", "no");
        button("Yes", "yes");
    }

    private void updateLoadDataButton() {
        Table saveFile = (Table) deleteSaveButton.getParent();
        Actor actor = new StartSaveButton("", getSkin(), root, saveSlot);
        for (int i = 0; i < saveFile.getCells().size; i++) {
            if (saveFile.getCells().get(i).getActor() instanceof StartSaveButton) {
                Cell<?> cell = saveFile.getCells().get(i);
                cell.setActor(actor);
            }
        }
    }
}
