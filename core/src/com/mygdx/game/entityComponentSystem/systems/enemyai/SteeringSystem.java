package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.components.Player;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Spawn;

public class SteeringSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> entities;
    Entity player;

    public SteeringSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(10);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = root.engine.getEntitiesFor(Families.enemies);
        player = root.engine.getEntitiesFor(Families.player).get(0);
        entities = root.engine.getEntitiesFor(Family.exclude(Player.class).get());
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
                case PURSUE:
                    setPursueBehavior(entity);
                    break;
                case RETURN:
                    setReturnBehavior(entity);
                    break;
                default:
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
        // set to priority steering in order to combine pursue and collision avoidance
        // the char moving needs proximity to determine chars in its vicinity
        // proximity (an area that stretches out from the char's position)
        // need to determine the char's neighbours before calc any steering output
        // override the findNeighbours method to determine neighbours

        Position pos = cg.getPosition(player);
        cg.getSteering(player).position.x = pos.x;
        cg.getSteering(player).position.y = pos.y;
        // constructor(owner-steering, owner-proximity)

        CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance<>(
                cg.getSteering(entity),cg.getDetectionProximity(entity));

        Arrive<Vector2> arrive = new Arrive<>(cg.getSteering(entity))
                .setArrivalTolerance(47)
                .setTimeToTarget(0.1f)
                .setTarget(cg.getSteering(player));
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(cg.getSteering(entity));
        // priority order of the iteration
        // returns the steering output of the first non-zero steering
        // collision avoidance is put first so
        prioritySteering.add(collisionAvoidance);
        prioritySteering.add(arrive);
        cg.getSteering(entity).steeringBehavior = prioritySteering;

//        cg.getSteering(entity).steeringBehavior = new Arrive<>(cg.getSteering(entity))
//                .setArrivalTolerance(3f)
//                .setDecelerationRadius(10f)
//                .setTimeToTarget(0.1f)
//                .setTarget(cg.getSteering(player));
    }

    private void setReturnBehavior(Entity entity) {
        Spawn spawn = cg.getSpawn(entity);
        GameLocation spawnPosition = new GameLocation(spawn.spawnPosX, spawn.spawnPosY);

        CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance<>(
                cg.getSteering(entity),
                cg.getDetectionProximity(entity));

        Arrive<Vector2> arrive = new Arrive<>(cg.getSteering(entity))
                .setArrivalTolerance(3)
                .setDecelerationRadius(10)
                .setTimeToTarget(0.1f)
                .setTarget(spawnPosition);

        cg.getSteering(entity).steeringBehavior = new Arrive<>(cg.getSteering(entity))
                .setArrivalTolerance(3f)
                .setDecelerationRadius(10f)
                .setTimeToTarget(0.1f)
                .setTarget(spawnPosition);

        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(cg.getSteering(entity));
        prioritySteering.add(collisionAvoidance);
        prioritySteering.add(arrive);

        cg.getSteering(entity).steeringBehavior = prioritySteering;
    }
}
