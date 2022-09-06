package com.mygdx.game.utils;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.engine.utils.entities.PlayerEntity;

public class SaveDataLoader {
    Json json = new Json();

    public SaveDataLoader() {
        json = new Json();
    }

    public PlayerEntity load(String serializedData) {
        PlayerEntity player = new PlayerEntity();

        return player;
    }
}
