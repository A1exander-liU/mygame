package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.mygdx.game.GameLocation;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.components.Enemy;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Spawn;

public class SteeringSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;
    Entity player;

    public SteeringSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = root.engine.getEntitiesFor(Families.enemies);
        player = root.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public Engine getEngine() {
        return root.engine;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            switch (cg.getEnemy(entity).state) {
                case WANDER:
                    break;
                case PURSUE:
                    setPursueBehavior(entity);
                    break;
                case RETURN:
                    setReturnBehavior(entity);
                    break;
            }
        }
        GdxAI.getTimepiece().update(delta);
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            cg.getSteering(entity).update(delta);
        }
    }

    private void setPursueBehavior(Entity entity) {
        Position pos = cg.getPosition(player);
        cg.getSteering(player).position.x = pos.x;
        cg.getSteering(player).position.y = pos.y;
        cg.getSteering(entity).steeringBehavior = new Arrive<>(cg.getSteering(entity))
                .setArrivalTolerance(3f)
                .setDecelerationRadius(10f)
                .setTimeToTarget(0.1f)
                .setTarget(cg.getSteering(player));
    }

    private void setReturnBehavior(Entity entity) {
        Spawn spawn = cg.getSpawn(entity);
        GameLocation spawnPosition = new GameLocation(spawn.spawnPosX, spawn.spawnPosY);
        cg.getSteering(entity).steeringBehavior = new Arrive<>(cg.getSteering(entity))
                .setArrivalTolerance(3f)
                .setDecelerationRadius(10f)
                .setTimeToTarget(0.1f)
                .setTarget(spawnPosition);
    }

}
