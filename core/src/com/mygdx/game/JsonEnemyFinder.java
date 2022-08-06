package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Objects;

//JsonReader jsonReader = new JsonReader();
//        JsonValue base = jsonReader.parse(Gdx.files.internal("enemies.json"));
//        JsonValue enemies = base.get("enemies");
//        for (JsonValue enemy : enemies) {
//        System.out.println(enemy.getString("name"));
//        }

public class JsonEnemyFinder extends JsonReader {
    private final JsonValue enemies;

    public JsonEnemyFinder() {
        super();
        enemies = super.parse(Gdx.files.internal("gameData/enemies.json")).get("enemies");
    }

    public JsonValue getEnemies() {
        return enemies;
    }

    public JsonValue findByEnemyName(String name) {
        // search and find object based on enemy name
        for (JsonValue enemy : enemies) {
            if (Objects.equals(enemy.getString("name"), name))
                return enemy;
        }
        return null;
    }
}
