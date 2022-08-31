package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.RemovableComponent;

public class EnemyDeathSystem extends EntitySystem {

    ImmutableArray<Entity> enemies;

    public EnemyDeathSystem() {
        super(102);
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);

    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            if (enemyIsDead(enemy)) enemy.add(new RemovableComponent());
        }
    }

    private boolean enemyIsDead(Entity enemy) {
        return Mappers.parameter.get(enemy).health.currentHealth <= 0;
    }
}
