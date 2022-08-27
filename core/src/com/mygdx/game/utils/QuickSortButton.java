package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.Rarity;

import java.util.HashMap;

public class QuickSortButton extends ImageButton {
    Entity player;

    public QuickSortButton(Skin skin) {
        super(skin);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("clicked");
                quickSortInventory();
            }
        });
    }

    private void quickSortInventory() {
        // sort by rarity descending (Mythical -> Common)
        // item order: main, off, armor, accessories, consumables, materials
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        for (int i = 0; i < inventorySlots.size; i++) {
            // last element has nothing to compare to
            for (int j = 0; j < inventorySlots.size - 1; j++) {
                InventorySlot thisSlot = inventorySlots.get(j);
                InventorySlot nextSlot = inventorySlots.get(j + 1);
                // if both slots are empty, don't need to sort
                if (thisSlot.isEmpty() && nextSlot.isEmpty()) continue;

                Entity thisItem = thisSlot.getOccupiedItem();
                Entity nextItem = nextSlot.getOccupiedItem();

                // slots with items then slots w/o items
                if (thisSlot.isEmpty()) swapSlots(thisSlot, nextSlot);
                // check if item types are the same
                else if (sameItemType(thisItem, nextItem))
                    // check if nextItem has higher rarity
                    if (hasHigherRarity(nextItem, thisItem)) swapSlots(thisSlot, nextSlot);
                // check if the item types aren't the same
                else if (!sameItemType(thisItem, nextItem))
                    // if nextItem has higher item priority
                    if (hasHigherPriority(nextItem, thisItem)) swapSlots(thisSlot, nextSlot);

            }
        }
    }

    private void swapSlots(InventorySlot thisSlot, InventorySlot nextSlot) {
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        inventorySlots.set(inventorySlots.indexOf(thisSlot, true), nextSlot);
        inventorySlots.set(inventorySlots.indexOf(nextSlot, true), thisSlot);
    }

    private boolean sameItemType(Entity thisItem, Entity nextItem) {
        return Mappers.inventoryItem.get(thisItem).acceptedItemType == Mappers.inventoryItem.get(nextItem).acceptedItemType;
    }

    private boolean hasHigherRarity(Entity thisItem, Entity nextItem) {
        return Mappers.rarity.get(thisItem).rarity.getRank() > Mappers.rarity.get(nextItem).rarity.getRank();
    }

    private boolean hasHigherPriority(Entity thisItem, Entity nextItem) {
        return Mappers.inventoryItem.get(thisItem).acceptedItemType.getRank() > Mappers.inventoryItem.get(nextItem).acceptedItemType.getRank();
    }
}
