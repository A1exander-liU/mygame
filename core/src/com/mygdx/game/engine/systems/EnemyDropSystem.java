package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.LootGenerator;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;

import java.util.HashMap;

public class EnemyDropSystem extends EntitySystem {

    ImmutableArray<Entity> enemies;
    LootGenerator lootGenerator = new LootGenerator();
    Entity player;

    public EnemyDropSystem() {
        super(103);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            if (Mappers.removable.get(enemy) != null) {
                String deadEnemyName = Mappers.name.get(enemy).name;
                HashMap<Integer, Integer> loot = lootGenerator.generateDrops(deadEnemyName);
                addToCoinPouch(loot);
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
}
