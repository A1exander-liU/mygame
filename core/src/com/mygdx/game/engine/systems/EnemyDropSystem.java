package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;

public class EnemyDropSystem extends EntitySystem {

    ImmutableArray<Entity> enemies;

    public EnemyDropSystem() {
        super(103);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            if (Mappers.removable.get(enemy) != null) {
                String deadEnemyName = Mappers.name.get(enemy).name;
            }
        }
    }
}
