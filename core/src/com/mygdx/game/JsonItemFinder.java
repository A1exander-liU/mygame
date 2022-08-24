package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Objects;

public class JsonItemFinder extends JsonReader {
    private final JsonValue materials;
    private final JsonValue weapons;

    public JsonItemFinder() {
        super();
        materials = super.parse(Gdx.files.internal("gameData/itemData/materials.json")).get("materials");
        weapons = super.parse(Gdx.files.internal("gameData/itemData/weapons.json"));
    }

    public JsonValue findMaterialByName(String itemName) {
        for (JsonValue material : materials) {
            if (Objects.equals(material.getString("name"), itemName))
                return material;
        }
        return null;
    }

    public JsonValue findWeaponByName(String weaponName) {
        for (JsonValue weapon : weapons) {
            if (Objects.equals(weapon.getString("name"), weaponName))
                return weapon;
        }
        return null;
    }

}
