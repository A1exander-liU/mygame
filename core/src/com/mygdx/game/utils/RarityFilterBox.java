package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.Rarity;

public class RarityFilterBox extends SelectBox<String> {

    Rarity currentFilter;
    Entity player;
    Array<InventorySlot> currentInventory;

    public RarityFilterBox(Skin skin) {
        super(skin);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        setItems("All", "Common", "Uncommon", "Rare", "Epic", "Legendary", "Mythical");
        getList().setAlignment(Align.left);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                determineRarityFilter(getSelected());
                filter();
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

    private void filter() {
        currentInventory = Mappers.inventory.get(player).inventorySlots;
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
    }

}
