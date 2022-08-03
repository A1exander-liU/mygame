package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObjects;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.SpawnArea;
import com.mygdx.game.utils.EntityTextureObject;

public class EntityRemovalSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> spawns;

    public EntityRemovalSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(99);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        spawns = MyGame.engine.getEntitiesFor(Families.spawns);
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            Enemy enemy = cg.getEnemy(entity);
            if (!enemy.isAlive) {
                removeOwner(entity);
                MyGame.engine.removeEntity(entity);
                removeFromMap(entity);
            }
        }
    }

    private void removeFromMap(Entity entity) {
        MapObjects collisions = gameMapProperties.getMapLayer(GameMapProperties.COLLISIONS).getObjects();
        for (int i = 0; i < collisions.getCount(); i++) {
            if (collisions.get(i) instanceof EntityTextureObject) {
                EntityTextureObject textureObject = (EntityTextureObject) collisions.get(i);
                if (textureObject.getOwner() == entity) {
                    collisions.remove(collisions.get(i));
                    break;
                }
            }
        }
    }

    private void removeOwner(Entity entity) {
        for (int i = 0; i < spawns.size(); i++) {
            Entity spawn = spawns.get(i);
            SpawnArea spawnArea = cg.getSpawnArea(spawn);
            if (spawnArea.owner == entity) {
                spawnArea.owner = null;
                spawnArea.lastTimeOfDeath = TimeSystem.time;
            }
        }
    }

}
