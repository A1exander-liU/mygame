package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.utils.InventorySlot;

public class ItemInfoDialog extends Dialog {

    InventorySlot inventorySlot;
    Entity item;

    public ItemInfoDialog(String title, Skin skin, InventorySlot inventorySlot) {
        super(title, skin);
        this.inventorySlot = inventorySlot;
        item = inventorySlot.getOccupiedItem();
    }

    public ItemInfoDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public ItemInfoDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);

    }

    {
        Label label = new Label("Close?", getSkin(), "pixel2D", Color.BLACK);

        text(label);
        button("Yes", "yes");
        button("No", "no");

        getContentTable().pad(5);
        getButtonTable().pad(5);
    }

    @Override
    protected void result(Object object) {
        if (object.equals("yes")) {
            hide();
        }
        else {
            cancel();
        }

    }

}
