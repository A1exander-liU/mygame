package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.engine.Rarity;

import java.util.HashMap;
import java.util.Objects;

public class EquipmentGenerator {

    JsonValue items;

    public EquipmentGenerator() {
        JsonReader reader = new JsonReader();
        items = reader.parse(Gdx.files.internal("gameData/itemData/items.json"));
    }

    public void generateEquipment(int itemId) {
        for (JsonValue item: items) {
            // determine the item
            if (item.getInt("id") == itemId && isEquipment(item)) {
                makeEquipment(item);
            }
        }
    }

    private boolean isEquipment(JsonValue item) {
        return !Objects.equals(item.getString("itemType"), "material")
                && !Objects.equals(item.getString("itemType"), "consumable");
    }

    private void makeEquipment(JsonValue item) {
        Rarity itemRarity = rollForRarity();
        switch (item.getString("id")) {
            case "main":
            case "off":
            case "accessory":
            case "head":
            case "torso":
            case "leg":
            case "feet":
        }
    }

    private Rarity rollForRarity() {
        HashMap<Rarity, Integer> rarityWeights = new HashMap<>();
        rarityWeights.put(Rarity.COMMON, 52);
        rarityWeights.put(Rarity.UNCOMMON, 21); 
        rarityWeights.put(Rarity.RARE, 15);
        rarityWeights.put(Rarity.EPIC, 8);
        rarityWeights.put(Rarity.LEGENDARY, 3);
        rarityWeights.put(Rarity.MYTHICAL, 1);
    }
}
