package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
        text("Close?");
        button("Yes", true);
        button("No", false);
    }

    @Override
    protected void result(Object object) {

    }

}
