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

    HashMap<Rarity, RarityValue> rarities;

    enum RarityValue {
        COMMON    (1),
        UNCOMMON  (2),
        RARE      (3),
        EPIC      (4),
        LEGENDARY (5),
        MYTHICAL  (6);

        int value;

        public int getValue() {
            return value;
        }

        RarityValue(int value) {
            this.value = value;
        }
    }

    enum Type {
        MAIN       (1),
        OFF        (2),
        ACCESSORY  (3),
        HEAD       (4),
        TORSO      (5),
        LEG        (6),
        FEET       (7),
        CONSUMABLE (8),
        MATERIAL   (9);

        int value;

        public int getValue() {
            return value;
        }

        Type(int value) {
            this.value = value;
        }
    }

    public QuickSortButton(Skin skin) {
        super(skin);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        makeRarities();
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
                if (thisSlot.getOccupiedItem() == null && nextSlot == null) continue;
                if (thisSlot.isEmpty()) {
                    inventorySlots.set(j, nextSlot);
                    inventorySlots.set(j + 1, thisSlot);
                }
            }
        }
    }

    private void makeRarities() {
        rarities.put(Rarity.COMMON, RarityValue.COMMON);
        rarities.put(Rarity.UNCOMMON, RarityValue.UNCOMMON);
        rarities.put(Rarity.RARE, RarityValue.RARE);
        rarities.put(Rarity.EPIC, RarityValue.EPIC);
        rarities.put(Rarity.LEGENDARY, RarityValue.LEGENDARY);
        rarities.put(Rarity.MYTHICAL, RarityValue.MYTHICAL);
    }
}
