package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Objects;

public class JsonItemFinder extends JsonReader {
    private final JsonValue materials;
    private final JsonValue weapons;
    private final JsonValue headArmours;
    private final JsonValue chestArmours;
    private final JsonValue legArmours;
    private final JsonValue feetArmours;

    public JsonItemFinder() {
        super();
        materials = super.parse(Gdx.files.internal("gameData/itemData/materials.json")).get("materials");
        weapons = super.parse(Gdx.files.internal("gameData/itemData/weapons.json")).get("weapons");
        headArmours = super.parse(Gdx.files.internal("gameData/itemData/armourPieces/headArmours.json")).get("headArmours");
        chestArmours = super.parse(Gdx.files.internal("gameData/itemData/armourPieces/chestArmours.json")).get("chestArmours");
        legArmours = super.parse(Gdx.files.internal("gameData/itemData/armourPieces/legArmours.json")).get("legArmours");
        feetArmours = super.parse(Gdx.files.internal("gameData/itemData/armourPieces/feetArmours.json")).get("feetArmours");
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

    public JsonValue findChestArmourByName(String chestArmourName) {
        for (JsonValue chestArmour : chestArmours) {
            if (Objects.equals(chestArmour.getString("name"), chestArmourName))
                return chestArmour;
        }
        return null;
    }

}
