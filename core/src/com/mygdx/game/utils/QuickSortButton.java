package com.mygdx.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InventoryUi;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.Rarity;
import com.mygdx.game.engine.components.inventory.items.shared.QuantityComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        for (int i = 0; i < inventorySlots.size - 1; i++) {
            // last element has nothing to compare to
            for (int j = 0; j < inventorySlots.size - 1; j++) {
                InventorySlot thisSlot = inventorySlots.get(j);
                InventorySlot nextSlot = inventorySlots.get(j + 1);
                // if both slots are empty, don't need to sort
                if (thisSlot.isEmpty() && nextSlot.isEmpty()) continue;

                if (nextSlot.isEmpty()) continue;

                Entity thisItem = thisSlot.getOccupiedItem();
                Entity nextItem = nextSlot.getOccupiedItem();

                // slots with items then slots w/o items
                if (thisSlot.isEmpty() && !nextSlot.isEmpty()) swapSlots(thisSlot, nextSlot);
                // check if item types are the same
                else if (sameItemType(thisItem, nextItem)) {
                    System.out.println("same item");
                    // check if nextItem has higher rarity
                    if (hasHigherRarity(nextItem, thisItem)) swapSlots(thisSlot, nextSlot);
                    else if (earlierInAlphabet(nextItem, thisItem)) swapSlots(thisSlot, nextSlot);
                    else if (sameItem(thisItem, nextItem) && stackable(thisItem)) {
                        InventoryUi ui = new InventoryUi();
                        ui.stackItems(thisSlot, nextSlot);
                        if (hasHigherQuantity(nextItem, thisItem)) swapSlots(thisSlot, nextSlot);
                    }
                }
                // check if the item types aren't the same
                else if (!sameItemType(thisItem, nextItem)) {
                    System.out.println("different item");
                    // if nextItem has higher item priority
                    if (hasHigherPriority(nextItem, thisItem)) swapSlots(thisSlot, nextSlot);
                }
            }
        }

    }

    private void swapSlots(InventorySlot thisSlot, InventorySlot nextSlot) {
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        int thisSlotIndex = inventorySlots.indexOf(thisSlot, true);
        int nextSlotIndex = inventorySlots.indexOf(nextSlot, true);
        inventorySlots.set(thisSlotIndex, nextSlot);
        inventorySlots.set(nextSlotIndex, thisSlot);
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

    private boolean sameItem(Entity thisItem, Entity nextItem) {
        return Objects.equals(Mappers.name.get(thisItem).name, Mappers.name.get(nextItem).name);
    }

    private boolean earlierInAlphabet(Entity thisItem, Entity nextItem) {
        int thisItemCharValue = Mappers.name.get(thisItem).name.toCharArray()[0];
        int nextItemCharValue = Mappers.name.get(nextItem).name.toCharArray()[0];
        return thisItemCharValue < nextItemCharValue;
    }

    private boolean stackable(Entity item) {
        return Mappers.quantity.get(item) != null;
    }

    private boolean hasHigherQuantity(Entity thisItem, Entity nextItem) {
        return Mappers.quantity.get(thisItem).quantity > Mappers.quantity.get(nextItem).quantity;
    }
}
