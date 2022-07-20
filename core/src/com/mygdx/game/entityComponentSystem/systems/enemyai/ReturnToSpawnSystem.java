package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.Spawn;

public class ReturnToSpawnSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;

    public ReturnToSpawnSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = root.engine.getEntitiesFor(Families.enemies);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {
        GdxAI.getTimepiece().update(delta);
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            Enemy enemy = cg.getEnemy(entity);
            Spawn spawn = cg.getSpawn(entity);
            if (enemy.state == Enemy.States.RETURN) {
                cg.getSteering(entity).steeringBehavior = returnToSpawn(entity);
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            cg.getSteering(entity).update(delta);
        }
    }

    private Arrive<Vector2> returnToSpawn(Entity enemy) {
        Spawn spawn = cg.getSpawn(enemy);
        GameLocation spawnPosition = new GameLocation();
        spawnPosition.setPosition(spawn.spawnPosX, spawn.spawnPosY);
        return new Arrive<>(cg.getSteering(enemy), spawnPosition)
                .setArrivalTolerance(3f)
                .setDecelerationRadius(10f)
                .setTimeToTarget(0.1f);
    }
}
