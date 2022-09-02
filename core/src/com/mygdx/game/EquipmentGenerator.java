package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.engine.ItemFactory;
import com.mygdx.game.engine.Rarity;

import java.util.HashMap;
import java.util.Objects;

public class EquipmentGenerator {

    JsonValue items;
    ItemFactory itemFactory;

    public EquipmentGenerator(ItemFactory itemFactory) {
        JsonReader reader = new JsonReader();
        items = reader.parse(Gdx.files.internal("gameData/itemData/items.json"));
        this.itemFactory = itemFactory;
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
                makeMain(item, itemRarity);
                break;
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

        float randomWeight = RandomNumberGenerator.roll();
        float runTotal = 0;
        for (Rarity rarity: rarityWeights.keySet()) {
            runTotal += rarityWeights.get(rarity);
            if (randomWeight < runTotal)
                return rarity;
        }
        return null;
    }

    private void makeMain(JsonValue item, Rarity itemRarity) {
        Entity weaponEntity = itemFactory.makeWeapon2(item, itemRarity);
        float modifier = 1;
        HashMap<Rarity, float[]> modifiers = generateModifiers();
        // common will have modifier of 1
        // uncommon: 1.01 - 1.2
        // rare: 1.21 - 1.4
        // epic: 1.41 - 1.6
        // legendary: 1.61 - 1.8
        // mythical: 1.81 - 2
        if (itemRarity != Rarity.COMMON) {
            float[] modifierRange = modifiers.get(itemRarity);
            modifier = RandomNumberGenerator.roll(modifierRange[0], modifierRange[1]);
        }
    }

    private HashMap<Rarity, float[]> generateModifiers() {
        HashMap<Rarity, float[]> hashMap = new HashMap<>();
        hashMap.put(Rarity.UNCOMMON, new float[] {1.01f, 1.2f});
        hashMap.put(Rarity.RARE, new float[] {1.21f, 1.4f});
        hashMap.put(Rarity.EPIC, new float[] {1.41f, 1.6f});
        hashMap.put(Rarity.LEGENDARY, new float[] {1.61f, 1.8f});
        hashMap.put(Rarity.MYTHICAL, new float[] {1.81f, 2});
        return hashMap;
    }
}
