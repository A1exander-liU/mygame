package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.engine.ItemFactory;
import com.mygdx.game.engine.Mappers;
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
        switch (item.getString("itemType")) {
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
        int affixesAmount = 0;
        HashMap<Rarity, float[]> modifiers = generateModifiers();
        HashMap<Rarity, int[]> affixes = generateAffixesAmount();
        // common will have modifier of 1
        // uncommon: 1.01 - 1.2
        // rare: 1.21 - 1.4
        // epic: 1.41 - 1.6
        // legendary: 1.61 - 1.8
        // mythical: 1.81 - 2
        if (itemRarity != Rarity.COMMON) {
            // get modifier of rarity
            float[] modifierRange = modifiers.get(itemRarity);
            modifier = RandomNumberGenerator.roll(modifierRange[0], modifierRange[1]);
        }
        int[] affixesRange = affixes.get(itemRarity);
        affixesAmount = RandomNumberGenerator.roll(affixesRange[0], affixesRange[1]);
        // dmg values will be multiplied by the modifier
        Mappers.weaponBaseStat.get(weaponEntity).minDmg = (int)Math.ceil(item.getInt("minDmg") * modifier);
        Mappers.weaponBaseStat.get(weaponEntity).maxDmg = (int)Math.ceil(item.getInt("maxDmg") * modifier);

        // generate affixes (extra stats)
        // hashmap with affix name and value
        // str, vit, dex (for now)

        // generate each affix
        String[] attributes = {"Strength", "Vitality", "Dexterity"};
        for (int i = 0; i < affixesAmount; i++) {
            String randomAffix = attributes[RandomNumberGenerator.roll(0, attributes.length)];
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

    private HashMap<Rarity, int[]> generateAffixesAmount() {
        HashMap<Rarity, int[]> hashMap = new HashMap<>();
        hashMap.put(Rarity.COMMON, new int[] {0, 2});
        hashMap.put(Rarity.UNCOMMON, new int[] {1, 3});
        hashMap.put(Rarity.RARE, new int[] {2, 4});
        hashMap.put(Rarity.EPIC, new int[] {3, 5});
        hashMap.put(Rarity.LEGENDARY, new int[] {4, 6});
        hashMap.put(Rarity.MYTHICAL, new int[] {7, 8});
        return hashMap;
    }
}
