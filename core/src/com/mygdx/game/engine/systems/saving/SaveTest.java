package com.mygdx.game.engine.systems.saving;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.MyGame;
import com.mygdx.game.SaveStates;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.utils.entities.PlayerEntity;
import com.mygdx.game.utils.saves.SavedPlayer;
import com.mygdx.game.utils.saves.SavedSlot;

public class SaveTest extends EntitySystem {
    MyGame root;

    public SaveTest(MyGame root) {
        this.root = root;
        String item = "item";
        Json json = new Json();
        String serialized = json.toJson(item);
        System.out.println(json.prettyPrint(serialized));

//        PlayerEntity player = new PlayerEntity();
//        SavedPlayer savedPlayer = new SavedPlayer(player);
//        String savedPlayerJson = json.toJson(savedPlayer);
//        System.out.println(json.prettyPrint(savedPlayerJson));
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            root.saveData.save((PlayerEntity) MyGame.engine.getEntitiesFor(Families.player).get(0));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            Json json = new Json();
            String lastSavedData = root.getSaveStates().getSlotSerializedData(SaveStates.SLOT_ONE);
            SavedPlayer lastSavedPlayer = json.fromJson(SavedPlayer.class, lastSavedData);
        }
    }
}
