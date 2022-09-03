package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.EquipmentGenerator;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.LootGenerator;
import com.mygdx.game.MyGame;
import com.mygdx.game.RandomNumberGenerator;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.ItemFactory;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.utils.EntityTextureObject;

import java.util.HashMap;
import java.util.Objects;

public class EnemyDropSystem extends EntitySystem {

    ItemFactory itemFactory;
    ImmutableArray<Entity> enemies;
    LootGenerator lootGenerator = new LootGenerator();
    EquipmentGenerator equipmentGenerator;
    Entity player;
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
                Array<Entity> lootEntities = new Array<>(0);
                for (Integer itemId: loot.keySet()) {
                    if (isMaterial(itemId)) {
                        JsonValue item = getItem(itemId);
                        if (item != null)
                            lootEntities.add(itemFactory.makeMaterial(item.getString("name"), loot.get(itemId)));
                    }
                    Entity equipment = equipmentGenerator.generateEquipment(itemId);
                    lootEntities.add(equipment);
                }
                displayDropsOnGround(lootEntities, enemy);
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
            if (item.getInt("id") == itemId)
                return Objects.equals(item.getString("itemType"), "material");
        }
        return false;
    }

    private JsonValue getItem(int itemId) {
        for (JsonValue item: items) {
            if (item.getInt("id") == itemId) return item;
        }
        return null;
    }

    private void displayDropsOnGround(Array<Entity> lootEntities, Entity enemy) {
        MapObjects drops = MyGame.gameMapProperties.getMapLayer(GameMapProperties.ENEMY_DROPS).getObjects();
        Position pos = Mappers.position.get(enemy);
        Vector2 enemyDeathPosition = new Vector2(pos.x, pos.y);
        for (int i = 0; i < lootEntities.size; i++) {
            Entity lootEntity = lootEntities.get(i);
            lootEntity.add(new Size());
            lootEntity.add(new Position());
            generatePositionNearEnemy(enemyDeathPosition, lootEntity, 20);
            Size size = Mappers.size.get(lootEntity);
            TextureRegion textureRegion = new TextureRegion(Mappers.sprite.get(lootEntity).texture, (int)size.width, (int)size.height);
            EntityTextureObject textureObject = new EntityTextureObject(textureRegion, lootEntity);
            textureObject.setX(Mappers.position.get(lootEntity).x);
            textureObject.setY(Mappers.position.get(lootEntity).y);
            MyGame.gameMapProperties.getMapLayer(GameMapProperties.ENEMY_DROPS).getObjects().add(textureObject);
        }
    }

    private void generatePositionNearEnemy(Vector2 enemyPos, Entity lootEntity, float radius) {
        // get random pos within certain radius of enemyPos
        float lootXPos = RandomNumberGenerator.roll(enemyPos.x - radius, enemyPos.x + radius);
        float lootYPos = RandomNumberGenerator.roll(enemyPos.y - radius, enemyPos.y + radius);
        Mappers.size.get(lootEntity).height = 10;
        Mappers.size.get(lootEntity).width = 10;
        Mappers.position.get(lootEntity).x = lootXPos;
        Mappers.position.get(lootEntity).y = lootYPos;
    }
}
