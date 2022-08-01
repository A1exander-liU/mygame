package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Enemy;

public class EntityRemovalSystem extends EntitySystem {
    ComponentGrabber cg;
    ImmutableArray<Entity> enemies;

    public EntityRemovalSystem(ComponentGrabber cg) {
        super(9);
        this.cg = cg;
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
        enemies = getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            Enemy enemy = cg.getEnemy(entity);
            if (!enemy.isAlive)
                MyGame.engine.removeEntity(entity);
        }
    }

    private ImmutableArray<Entity> getEnemies() {
        return MyGame.engine.getEntitiesFor(Families.enemies);
    }

}
