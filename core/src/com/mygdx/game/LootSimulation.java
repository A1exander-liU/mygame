package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Arrays;

public class LootSimulation {
    JsonValue drops;

    public LootSimulation() {
        JsonReader reader = new JsonReader();
        drops = reader.parse(Gdx.files.internal("gameData/dropTable.json"));
    }


    public void fightSlimes(int fights) {
        float totalWeight = 0;
        float[] weights = {};

        for (int i = 0; i < drops.size; i++) {
            // the object that holds name, always and chance
            JsonValue lootTable = drops.get(i);
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
        }
    }

}
