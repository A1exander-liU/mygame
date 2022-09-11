package com.mygdx.game.utils.ui.saving;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;

import java.util.Objects;

public class StartSaveButton extends TextButton {
    MyGame root;
    String saveSlot;
    // will be the button to click to start the game
    // will either be continue or new depending if the slot has save data
    public StartSaveButton(String text, Skin skin, MyGame root, String saveSlot) {
        super(text, skin);
        this.root = root;
        this.saveSlot = saveSlot;
        // if slot is empty, change to New
        if (Objects.equals(root.getSaveStates().getSlotSerializedData(saveSlot), "")) {
            setLabel(new Label("New", skin, "pixel2D", Color.BLACK));
        }
        // if the slot has save data, change to Continue
        else {
            setLabel(new Label("Continue", skin, "pixel2D", Color.BLACK));
        }
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startSave();
            }
        });
    }

    private void startSave() {
        String slotData = root.getSaveStates().getSlotSerializedData(saveSlot);
        root.saveData.setCurrentSlot(saveSlot);
        // when the slot has data
        if (!Objects.equals(slotData, "")) {
            root.saveData.load(slotData);
            root.changeScreen(MyGame.GAME_SCREEN);
        }
        // no save data in the slot, go to new player screen
        else {
            root.changeScreen(MyGame.NEW_PLAYER_SCREEN);
        }
    }
}
