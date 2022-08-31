package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;

public class EnemyDeathSystem extends EntitySystem {

    ImmutableArray<Entity> enemies;

    public EnemyDeathSystem() {
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);

    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
        }
    }
}
