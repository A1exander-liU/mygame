package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Objects;

public class JsonItemFinder extends JsonReader {
    private final JsonValue materials;

    public JsonItemFinder() {
        super();
        materials = super.parse(Gdx.files.internal("gameData/itemData/materials.json")).get("materials");
    }

    public JsonValue findMaterialByName(String itemName) {
        for (JsonValue material : materials) {
            if (Objects.equals(material.getString("name"), itemName))
                return material;
        }
        return null;
    }

}
