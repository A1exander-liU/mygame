package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.engine.Rarity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class LootSimulation {
    JsonValue drops;

    public LootSimulation() {
        JsonReader reader = new JsonReader();
        drops = reader.parse(Gdx.files.internal("gameData/dropTable.json"));
    }

    public void fightSlimes(int fights) {
        float totalWeight = 0;
        float[] weights = {};

        // get slime loot table object
        JsonValue lootTable = getEnemyLootTable("slime");

        // the object array that holds items that have a chance to drop
        JsonValue chance = lootTable.get("chance");
        JsonValue always = lootTable.get("always");
        // loop through chance item drops and add up the weights
        for (JsonValue chanceDrop: chance) {
            totalWeight += chanceDrop.getFloat("weight");
            // array size is fixed need to make copy and increase size by one each
            // time to add new element
            weights = Arrays.copyOf(weights, weights.length + 1);
            // copied array is same array with one new element at end
            weights[weights.length - 1] = chanceDrop.getFloat("weight");
        }

        Random random = new Random();

        HashMap<String, Integer> totalLoot = new HashMap<>();
        for (int i = 0; i < fights; i++) {
            // get a random weight
            float randomWeight = random.nextFloat() * totalWeight;
            float choice = 0;
            float runTotal = 0;

            for (int j = 0; j < weights.length; j++) {
                runTotal += weights[j];
                // when randomWeight is less than runTotal, then get item dropped
                // with the weight of the last addition

                // the individual weights = 1, 10, 50
                // ex. to get nothing, you would have to end up adding
                // its weight (50 for slime)
                // if you loop through and add the other two (1, 10) that only gets 11
                // so in order to get nothing the random weight has to be >= 11
                // 12
                // if 12 < 10 nope
                // if 12 < 11 nope
                // if 12 < 61 yes
                if (randomWeight < runTotal) {
                    choice = weights[j];
                    break;
                }
            }

            // get the item with the weight
            for (JsonValue dropChance: chance) {
                if (choice == dropChance.getFloat("weight")) {
                    if (totalLoot.containsKey(dropChance.getString("item"))) {
                        totalLoot.put(dropChance.getString("item"), totalLoot.get(dropChance.getString("item")) + 1);
                    }
                    else {
                        totalLoot.put(dropChance.getString("item"), 1);
                    }
                }
            }
            for (JsonValue item: always) {
                String name = item.getString("item");
                int max = item.getInt("max");
                // generate random material amount
                int randomAmount = (random.nextInt(max)) + 1;
                // set or increment item count depending if it already exists
                if (totalLoot.containsKey(name)) totalLoot.put(name, totalLoot.get(name) + randomAmount);
                else totalLoot.put(name, randomAmount);
            }
        }
        System.out.println("Spoils");
        for (String item: totalLoot.keySet())
            System.out.println(item + ": " + totalLoot.get(item));
    }

    public void equipmentGeneration(String equipmentName) {
        HashMap<Rarity, Integer> rarityWeights = generateRarityWeights();
        JsonValue equipment = findEquipment(equipmentName);
        Rarity randomRarity = generateEquipmentRarity(rarityWeights);
        float statBoost = getStatBoost(randomRarity);
    }

    private JsonValue getEnemyLootTable(String enemyName) {
        for (JsonValue drop: drops) {
            if (Objects.equals(drop.getString("name"), enemyName)) return drop;
        }
        return null;
    }

    private HashMap<Rarity, Integer> generateRarityWeights() {
        HashMap<Rarity, Integer> hashMap = new HashMap<>();
        hashMap.put(Rarity.COMMON, 85);
        hashMap.put(Rarity.UNCOMMON, 50);
        hashMap.put(Rarity.RARE, 25);
        hashMap.put(Rarity.EPIC, 10);
        hashMap.put(Rarity.LEGENDARY, 3);
        hashMap.put(Rarity.MYTHICAL, 1);
        return hashMap;
    }

    private JsonValue findEquipment(String equipmentName) {
        JsonItemFinder itemFinder = new JsonItemFinder();
        JsonValue equipmentData = itemFinder.findWeaponByName(equipmentName);
        if (equipmentData == null) equipmentData = itemFinder.findHeadArmourByName(equipmentName);
        else return equipmentData;

        if (equipmentData == null) equipmentData = itemFinder.findChestArmourByName(equipmentName);
        else return equipmentData;

        if (equipmentData == null) equipmentData = itemFinder.findLegArmourByName(equipmentName);
        else return equipmentData;

        if (equipmentData == null) equipmentData = itemFinder.findFeetArmourByName(equipmentName);
        return equipmentData;
    }

    private Rarity generateEquipmentRarity(HashMap<Rarity, Integer> rarityWeights) {
        float totalWeight = 0;
        // sum up the weights
        for (Integer weight: rarityWeights.values()) totalWeight += weight;
        Random random = new Random();

        float randomWeight = random.nextFloat() * totalWeight;
        float runTotal = 0;
        Rarity randomRarity = null;

        for (Rarity rarity: rarityWeights.keySet()) {
            runTotal += rarityWeights.get(rarity);
            if (randomWeight < runTotal) {
                randomRarity = rarity;
                break;
            }
        }
        return randomRarity;
    }

    private float getStatBoost(Rarity rarity) {
        if (rarity == Rarity.COMMON) return 1;
        if (rarity == Rarity.UNCOMMON) return 1.2f;
        if (rarity == Rarity.RARE) return 1.4f;
        if (rarity == Rarity.EPIC) return 1.6f;
        if (rarity == Rarity.LEGENDARY) return 1.8f;
        if (rarity == Rarity.MYTHICAL) return 2;
        return 0;
    }
}
