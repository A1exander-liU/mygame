package com.mygdx.game.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.engine.Mappers;

public class InventoryFilterListener extends ChangeListener {

    RarityFilterBox rarityFilterBox;
    ItemFilterBox itemFilterBox;

    public InventoryFilterListener(RarityFilterBox rarityFilterBox, ItemFilterBox itemFilterBox) {
        this.rarityFilterBox = rarityFilterBox;
        this.itemFilterBox = itemFilterBox;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        System.out.println("InventoryFilterListener");
        System.out.println(rarityFilterBox.currentFilter);
        System.out.println(itemFilterBox.currentFilter);
        filter();
    }

    private void filter() {
        if (rarityFilterBox.currentFilter == null && itemFilterBox.currentFilter == null) {
            // both have the same player and current inventory so it doesn't matter
            Mappers.inventory.get(rarityFilterBox.player).inventorySlots = rarityFilterBox.currentInventory;
        }
    }
}
