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
        getStyle().listStyle.font.getData().setScale(.8f);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                determineRarityFilter(getSelected());
                filter();
            }
        });
    }

    public RarityFilterBox(Skin skin, String actorName) {
        super(skin);
        setName(actorName);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        setItems("All", "Common", "Uncommon", "Rare", "Epic", "Legendary", "Mythical");
        getList().setAlignment(Align.left);
        getStyle().listStyle.font.getData().setScale(.8f);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                determineRarityFilter(getSelected());
                filter();
            }
        });
    }

    public Array<InventorySlot> getCurrentInventory() {
        return currentInventory;
    }

    public void setCurrentInventory(Array<InventorySlot> currentInventory) {
        this.currentInventory = currentInventory;
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
        System.out.println(getSelected());
        Array<InventorySlot> filtered = new Array<>(0);
        // use new array to store all filtered items
        // once finished adding all filtered items (loop through currentInventory)
        // set inventorySlots in InventoryComponent to filteredArray
        // once inventory is exited or filter is back to all -> set with currentInventory

        // return inventory back to normal and immediately return
        // the filter will be under this if statement
        if (currentFilter == null) {
            Mappers.inventory.get(player).inventorySlots = currentInventory;
            return;
        }
        // check the rarity
        for (int i = 0; i < currentInventory.size; i++) {
            if (!currentInventory.get(i).isEmpty() && Mappers.rarity.get(currentInventory.get(i).getOccupiedItem()).rarity == currentFilter)
                filtered.add(currentInventory.get(i));
        }
        // add in all the empty slots
        for (int i = 0; i < currentInventory.size; i++) {
            if (currentInventory.get(i).isEmpty())
                filtered.add(currentInventory.get(i));
        }
        // set inventorySlots to filtered array to have it rendered
        Mappers.inventory.get(player).inventorySlots = filtered;
    }

}
