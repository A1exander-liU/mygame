package com.mygdx.game.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.engine.Rarity;

public class RarityFilterBox extends SelectBox<String> {

    Rarity currentFilter;

    public RarityFilterBox(Skin skin) {
        super(skin);
        setItems("All", "Common", "Uncommon", "Rare", "Epic", "Legendary", "Mythical");
        getList().setAlignment(Align.left);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                determineRarityFilter(getSelected());
            }
        });
    }

    private void determineRarityFilter(String selectedFilter) {
        switch (selectedFilter) {
            case "All":
                // null will mean all
                currentFilter = null;
                break;
            case "Common":
                currentFilter = Rarity.COMMON;
                break;
            case "Uncommon":
                currentFilter = Rarity.UNCOMMON;
                break;
            case "Rare":
                currentFilter = Rarity.RARE;
                break;
            case "Epic":
                currentFilter = Rarity.EPIC;
                break;
            case "Legendary":
                currentFilter = Rarity.LEGENDARY;
                break;
            case "Mythical":
                currentFilter = Rarity.MYTHICAL;
                break;
        }
    }

}
