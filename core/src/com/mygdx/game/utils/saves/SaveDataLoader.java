package com.mygdx.game.utils.saves;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.systems.saving.SaveTest;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.utils.entities.PlayerEntity;
import com.mygdx.game.utils.ui.InventorySlot;

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

        loadInventory(player, savedPlayer);

        return player;
    }

    private void loadInventory(PlayerEntity player, SavedPlayer savedPlayer) {
        // convert all SavedItem to entities
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        for (int i = 0; i < savedPlayer.inventoryItems.size; i++) {
            Entity item = new Entity();
            SavedItem savedItem = savedPlayer.inventoryItems.get(i);
            // if null means slot was empty
            if (savedItem == null)
                inventorySlots.get(i).setOccupiedItem(null);
            else {
                item.add(savedItem.inventoryItemComponent);
                item.add(savedItem.name);
                item.add(new Sprite());
                Mappers.sprite.get(item).texture = new Texture(Gdx.files.internal(savedItem.itemImgPath));
                item.add(savedItem.rarityComponent);
                item.add(savedItem.descriptionComponent);
                // if stackable add the necessary components
                if (savedItem.stackableComponent != null) {
                    item.add(savedItem.quantityComponent);
                    item.add(savedItem.stackableComponent);
                }
                // if its weapon or armour add the baseStatComponent and Affix
                if (savedItem.weaponBaseStatComponent != null) {
                    item.add(savedItem.weaponBaseStatComponent);
                    item.add(savedItem.affixesComponent);
                }
                if (savedItem.armourBaseStatComponent != null) {
                    item.add(savedItem.armourBaseStatComponent);
                    item.add(savedItem.affixesComponent);
                }
            }
        }
    }
}
