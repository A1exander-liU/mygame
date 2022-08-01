package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;

public class EntityRemovalSystem extends EntitySystem {
    ComponentGrabber cg;
    ImmutableArray<Entity> enemies;

    public EntityRemovalSystem(ComponentGrabber cg) {
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
            
        }
    }

    private ImmutableArray<Entity> getEnemies() {
        return MyGame.engine.getEntitiesFor(Families.enemies);
    }

}
