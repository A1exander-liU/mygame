package com.mygdx.game.utils;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class RarityFilterBox extends SelectBox<String> {

    public RarityFilterBox(Skin skin) {
        super(skin);
        setItems("All", "Common", "Uncommon", "Rare", "Epic", "Legendary", "Mythical");
        getList().setAlignment(Align.left);

    }
}
