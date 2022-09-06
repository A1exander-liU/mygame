package com.mygdx.game.utils.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
        // add in rest of the components
        player.add(savedPlayer.camera);
        player.add(savedPlayer.expComponent);
        player.add(savedPlayer.health);
        player.add(savedPlayer.levelComponent);
        player.add(savedPlayer.manaComponent);
        player.add(savedPlayer.name);
        player.add(savedPlayer.orientation);
        player.add(savedPlayer.parameterComponent);
        player.add(savedPlayer.position);
        player.add(savedPlayer.size);
        player.add(savedPlayer.speed);

        // set values of other components already inside
        Mappers.sprite.get(player).texture = new Texture(Gdx.files.internal(savedPlayer.textureImgPath));

        return player;
    }
}
