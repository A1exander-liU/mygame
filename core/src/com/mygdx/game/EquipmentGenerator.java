package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class EquipmentGenerator {

    JsonValue items;

    public EquipmentGenerator() {
        JsonReader reader = new JsonReader();
        items = reader.parse(Gdx.files.internal("gameData/itemData/items.json"));
    }

    public void generateEquipment(int itemId) {
        
    }
}
