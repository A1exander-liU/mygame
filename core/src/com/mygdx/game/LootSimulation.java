package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class LootSimulation {
    JsonValue drops;

    public LootSimulation() {
        JsonReader reader = new JsonReader();
        drops = reader.parse(Gdx.files.internal("gameData/dropTable.json"));
    }

    public void fightSlimes(int fights) {
        JsonValue enemyLootTable;
        for (int i = 0; i < drops.size; i++) {
            JsonValue lootTable = drops.get(i);
            System.out.println(lootTable);
        }
    }

}
