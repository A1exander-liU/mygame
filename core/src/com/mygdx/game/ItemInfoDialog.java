package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.utils.InventorySlot;

public class ItemInfoDialog extends Dialog {

    public ItemInfoDialog(String title, Skin skin) {
        super(title, skin);
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
