package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

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

        JsonValue lootTable = null;

        for (JsonValue drop: drops) {
            if (Objects.equals(drop.getString("name"), "slime"))
                lootTable = drop;
        }
        // the object array that holds items that have a chance to drop
        JsonValue chance = lootTable.get("chance");
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
                // total = 61

                // r = 32
                // 10 -> if 32 < 10 no, keep adding
                // 11 -> if 32 < 11, no keep adding
                // 61 -> if 32 < 61, yes, choice is 50/nothing

                // r = 1;
                // 10 -> if 1 < 10

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
        }
        
    }

}
