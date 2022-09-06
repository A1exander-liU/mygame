package com.mygdx.game.utils.saves;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.engine.systems.saving.SaveTest;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.utils.entities.PlayerEntity;

public class SaveDataLoader {
    Json json = new Json();

    public SaveDataLoader() {
        json = new Json();
    }

    public PlayerEntity load(String serializedData) {
        SavedPlayer savedPlayer = json.fromJson(SavedPlayer.class, serializedData);
        PlayerEntity player = new PlayerEntity(false);
        // need to set component


        return player;
    }
}
