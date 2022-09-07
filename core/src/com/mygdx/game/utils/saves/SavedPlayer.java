package com.mygdx.game.utils.saves;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.engine.components.Camera;
import com.mygdx.game.engine.components.ExpComponent;
import com.mygdx.game.engine.components.Health;
import com.mygdx.game.engine.components.LevelComponent;
import com.mygdx.game.engine.components.ManaComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Orientation;
import com.mygdx.game.engine.components.ParameterComponent;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Speed;
import com.mygdx.game.engine.systems.saving.SaveTest;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.utils.entities.PlayerEntity;
import com.mygdx.game.utils.ui.InventorySlot;

public class SavedPlayer {
    Camera camera;
    ExpComponent expComponent;
    Health health;
    LevelComponent levelComponent;
    ManaComponent manaComponent;
    Name name;
    Orientation orientation;
    ParameterComponent parameterComponent;
    Position position;
    Size size;
    Speed speed;
    String textureImgPath;

    int coins;
    Array<SavedSlot> inventorySlots;
    Array<SavedSlot> equipSlots;
    Array<SavedItem> inventoryItems;
    Array<SavedItem> equipItems;

    public SavedPlayer() {}

    public SavedPlayer(PlayerEntity playerToSave) {
        camera = Mappers.camera.get(playerToSave);
        expComponent = Mappers.exp.get(playerToSave);
        health = Mappers.health.get(playerToSave);
        levelComponent = Mappers.level.get(playerToSave);
        manaComponent = Mappers.mana.get(playerToSave);
        name = Mappers.name.get(playerToSave);
        orientation = Mappers.orientation.get(playerToSave);
        parameterComponent = Mappers.parameter.get(playerToSave);
        position = Mappers.position.get(playerToSave);
        size = Mappers.size.get(playerToSave);
        speed = Mappers.speed.get(playerToSave);
        textureImgPath = ((FileTextureData) Mappers.sprite.get(playerToSave).texture.getTextureData()).getFileHandle().path();

        coins = Mappers.inventory.get(playerToSave).coinPouch;
        Array<InventorySlot> slots = Mappers.inventory.get(playerToSave).inventorySlots;
        Array<InventorySlot> equips = Mappers.inventory.get(playerToSave).equipSlots;
        inventorySlots = new Array<>();
        inventoryItems = new Array<>();
        for (int i = 0; i < slots.size; i++) {
            SavedSlot slot = new SavedSlot(slots.get(i));
            inventorySlots.add(slot);
            if (!slots.get(i).isEmpty())
                inventoryItems.add(new SavedItem(slots.get(i).getOccupiedItem()));
            else
                inventoryItems.add(null);
        }
        equipSlots = new Array<>();
        equipItems = new Array<>();
        for (int i = 0; i < equips.size; i++) {
            SavedSlot slot = new SavedSlot(equips.get(i));
            equipSlots.add(slot);
            if (!equips.get(i).isEmpty())
                equipItems.add(new SavedItem(equips.get(i).getOccupiedItem()));
            else
                equipItems.add(null);
        }
    }
}
