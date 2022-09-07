package com.mygdx.game.utils.saves;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.MyGame;
import com.mygdx.game.SaveStates;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.Steering;
import com.mygdx.game.engine.utils.componentutils.Mappers;
import com.mygdx.game.engine.utils.entities.PlayerEntity;
import com.mygdx.game.utils.ui.InventorySlot;

public class SaveData {
    Json json;
    MyGame root;
    String currentSlot;

    public SaveData(MyGame root) {
        this.root = root;
        json = new Json();
    }

    public String getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(String currentSlot) {
        this.currentSlot = currentSlot;
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
        player.add(new Steering(player));

        // set values of other components already inside
        Mappers.sprite.get(player).texture = new Texture(Gdx.files.internal(savedPlayer.textureImgPath));

        loadInventory(player, savedPlayer);
        loadEquipSlots(player, savedPlayer);

        MyGame.engine.addEntity(player);
        root.entityToMapAdder.addEntityToMap(player);

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
                inventorySlots.get(i).setOccupiedItem(item);

                MyGame.engine.addEntity(item);
            }
        }
        Mappers.inventory.get(player).inventorySlots = inventorySlots;
        Mappers.inventory.get(player).coinPouch = savedPlayer.coins;
    }

    private void loadEquipSlots(PlayerEntity player, SavedPlayer savedPlayer) {
        Array<InventorySlot> equipSlots = Mappers.inventory.get(player).equipSlots;
        for (int i = 0; i < savedPlayer.equipItems.size; i++) {
            Entity equippedItem = new Entity();
            SavedItem savedItem = savedPlayer.equipItems.get(i);
            if (savedItem == null)
                equipSlots.get(i).setOccupiedItem(null);
            else {
                equippedItem.add(savedItem.inventoryItemComponent);
                equippedItem.add(savedItem.name);
                equippedItem.add(new Sprite());
                Mappers.sprite.get(equippedItem).texture = new Texture(Gdx.files.internal(savedItem.itemImgPath));
                equippedItem.add(savedItem.rarityComponent);
                equippedItem.add(savedItem.descriptionComponent);
                // if its weapon or armour add the baseStatComponent and Affix
                if (savedItem.weaponBaseStatComponent != null) {
                    equippedItem.add(savedItem.weaponBaseStatComponent);
                    equippedItem.add(savedItem.affixesComponent);
                }
                if (savedItem.armourBaseStatComponent != null) {
                    equippedItem.add(savedItem.armourBaseStatComponent);
                    equippedItem.add(savedItem.affixesComponent);
                }
                equipSlots.get(i).setOccupiedItem(equippedItem);
                MyGame.engine.addEntity(equippedItem);
            }
        }
    }

    public void save(PlayerEntity player) {
        SavedPlayer savedPlayer = new SavedPlayer(player);
        json.setOutputType(JsonWriter.OutputType.minimal);
        json.setElementType(SavedPlayer.class, "inventorySlots", SavedSlot.class);
        String serialized = json.toJson(savedPlayer);
        // now when loading game need to recreate playerEntity
        // set the values with saved player components
        // need to recreate and set items in the inventory
        root.getSaveStates().setSlotData(currentSlot, serialized);
    }
}