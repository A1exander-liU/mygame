package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

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
}
