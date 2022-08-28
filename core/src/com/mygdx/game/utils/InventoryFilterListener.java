package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
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

        Array<InventorySlot> filtered = new Array<>(0);

        for (int i = 0; i < rarityFilterBox.currentInventory.size; i++) {
            InventorySlot slot = rarityFilterBox.currentInventory.get(i);
            // make sure slot is not empty and fits both rarity and item type criteria
            if (!slot.isEmpty() && matchesRarity(slot.getOccupiedItem()) && matchesItemType(slot.getOccupiedItem()))
                filtered.add(slot);
        }

        // add in all the empty slots afterwards
        for (int i = 0; i < rarityFilterBox.currentInventory.size; i++) {
            InventorySlot slot = rarityFilterBox.currentInventory.get(i);
            if (slot.isEmpty())
                filtered.add(slot);
        }

        Mappers.inventory.get(rarityFilterBox.player).inventorySlots = filtered;
    }

    private boolean matchesRarity(Entity item) {
        return Mappers.rarity.get(item).rarity == rarityFilterBox.currentFilter;
    }

    private boolean matchesItemType(Entity item) {
        return Mappers.inventoryItem.get(item).acceptedItemType == itemFilterBox.currentFilter;
    }
}
