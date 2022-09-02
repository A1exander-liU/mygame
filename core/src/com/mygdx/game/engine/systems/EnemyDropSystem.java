package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.EquipmentGenerator;
import com.mygdx.game.LootGenerator;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.ItemFactory;
import com.mygdx.game.engine.Mappers;

import java.util.HashMap;
import java.util.Objects;

public class EnemyDropSystem extends EntitySystem {

    ItemFactory itemFactory;
    ImmutableArray<Entity> enemies;
    LootGenerator lootGenerator = new LootGenerator();
    EquipmentGenerator equipmentGenerator;
    Entity player;
    Array<Entity> lootEntities = new Array<>(0);
    JsonReader reader = new JsonReader();
    JsonValue items;

    public EnemyDropSystem(ItemFactory itemFactory) {
        super(103);
        this.itemFactory = itemFactory;
        equipmentGenerator = new EquipmentGenerator(itemFactory);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        items = reader.parse(Gdx.files.internal("gameData/itemData/items.json"));
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            if (Mappers.removable.get(enemy) != null) {
                String deadEnemyName = Mappers.name.get(enemy).name;
                HashMap<Integer, Integer> loot = lootGenerator.generateDrops(deadEnemyName);
                addToCoinPouch(loot);
                for (Integer itemId: loot.keySet()) {
                    if (isMaterial(itemId)) {

                    }
                    Entity equipment = equipmentGenerator.generateEquipment(itemId);
                    lootEntities.add(equipment);
                }
            }
        }
    }

    private void addToCoinPouch(HashMap<Integer, Integer> loot) {
        for (Integer itemId: loot.keySet()) {
            // check if itemId is -1 (coins)
            if (itemId == -1) {
                // add coins to coinPouch
                Mappers.inventory.get(player).coinPouch += loot.get(itemId);
                // remove from loot, no longer needed since it was dded to coinPouch
                loot.remove(itemId);
                // only one coins object exists
                break;
            }
        }
    }

    private boolean isMaterial(int itemId) {
        for (JsonValue item: items) {
            return Objects.equals(item.getString("itemType"), "material");
        }
        return false;
    }

    private JsonValue getItem(int itemId) {
        for (JsonValue item: items) {
            if (item.getInt("itemId") == itemId) return item;
        }
        return null;
    }
}
