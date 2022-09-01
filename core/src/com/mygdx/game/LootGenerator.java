package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class LootGenerator {
    JsonValue drops;

    public LootGenerator() {
        JsonReader reader = new JsonReader();
        drops = reader.parse(Gdx.files.internal("gameData/dropTable.json"));
    }

    public HashMap<Integer, Integer> generateDrops(String enemyName) {
        JsonValue enemyLootTable = getEnemyLootTable(enemyName);
        if (enemyLootTable == null) return null;
        HashMap<Integer, Integer> loot = new HashMap<>();
        // roll for each item to see it dropped
        // roll random number, check if <= drop chance
        JsonValue anyOf = enemyLootTable.get("anyOf");
        // roll to see which one of the items dropped
        JsonValue oneOf = enemyLootTable.get("oneOf");
        rollAnyOf(loot, anyOf);
        rollOneOf(loot, oneOf);
        return loot;
    }

    private void rollAnyOf(HashMap<Integer, Integer> loot, JsonValue anyOf) {
        for (JsonValue item: anyOf) {
            float random = rollForItem();
            if (random <= item.getFloat("chance")) {
                int amount = rollAmount(item);
                loot.put(item.getInt("itemId"), amount);
            }
        }
    }

    private void rollOneOf(HashMap<Integer, Integer> loot, JsonValue oneOf) {
        float totalWeight = calcTotalWeight(oneOf);
        float[] itemWeights = generateItemWeights(oneOf);

        float randomWeight = generateRandomWeight(totalWeight);
        float runTotal = 0;
        // the index of the dropped item in the oneOf array
        float choice = determineDroppedItem(randomWeight, itemWeights);
        // chceck if dropped item to make sure it isn't nothing
        if (oneOf.get((int) choice).getInt("itemId") != -2) {
            int amount = rollAmount(oneOf.get((int) choice));
            loot.put(oneOf.get((int) choice).getInt("itemId"), amount);
        }
    }

    private float rollForItem() {
        Random random = new Random();
        return random.nextFloat() * 100;
    }

    private int rollAmount(JsonValue item) {
        // if min and max amount is the same
        int min = item.get("amount").getInt(0);
        int max = item.get("amount").getInt(1);
        if (min == max) {
            return min;
        }
        return roll(min, max);
    }

    private int roll(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) + min;
    }

    private float calcTotalWeight(JsonValue oneOf) {
        float totalWeight = 0;
        for (JsonValue item: oneOf) {
            totalWeight += item.getFloat("chance");
        }
        return totalWeight;
    }

    private float[] generateItemWeights(JsonValue oneOf) {
        float[] itemWeights = {};
        for (JsonValue item: oneOf) {
            // need to update array size so create new copy with one extra size
            itemWeights = Arrays.copyOf(itemWeights, itemWeights.length + 1);
            // add weight to end of the array
            itemWeights[itemWeights.length - 1] = item.getFloat("chance");
        }
        return itemWeights;
    }

    private float generateRandomWeight(float totalWeight) {
        Random random = new Random();
        return random.nextFloat() * totalWeight;
    }

    private float determineDroppedItem(float randomWeight, float[] itemWeights) {
        float runTotal = 0;
        for (int i = 0; i < itemWeights.length; i++) {
            runTotal += itemWeights[i];
            // 85, 10, 5

            // r = 30
            // 30 < 0
            // 30 < 85 -> 85 is choice

            // r = 90
            // 90 < 0
            // 90 < 85
            // 90 < 95 -> 10 is choice
            if (randomWeight < runTotal) return i;
        }
        return -1;
    }

    private JsonValue getEnemyLootTable(String enemyName) {
        for (JsonValue drop: drops) {
            if (Objects.equals(drop.getString("name"), enemyName)) return drop;
        }
        return null;
    }
}
