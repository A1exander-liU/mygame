package com.mygdx.game.engine.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.EnemyState;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Spawn;
import com.mygdx.game.engine.components.StateComponent;
import com.mygdx.game.engine.systems.TimeSystem;

import java.io.Reader;
import java.util.Random;
// 3 states: idle(random walk), hunting(pursue and attack player), flee(return)
public class SteeringSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> entities;
    Entity player;
    float elapsed = 0;

    Reader reader = null;

    public SteeringSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(10);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        entities = MyGame.engine.getEntitiesFor(Family.exclude(Player.class).get());
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
//        for (int i = 0; i < enemies.size(); i++) {
//            Entity entity = enemies.get(i);
//            switch (cg.getEnemy(entity).state) {
//                case PURSUE:
//                    setPursueBehavior(entity);
//                    break;
//                case RETURN:
//                    setReturnBehavior(entity);
//                    break;
//                case WANDER:
//                    setWanderBehavior(entity);
//                    break;
//                default:
//                    break;
//            }
//        }
        // steps through the behavior tree, basically just goes through the whole tree
        // and performs the updating going through all the composite and leaf tasks
//        for (int i = 0; i < enemies.size(); i++) {
//            Entity entity = enemies.get(i);
//            MobEntity enemy = (MobEntity) entity;
//            if (enemy.getBehaviorTree() != null)
//                enemy.getBehaviorTree().step();
//        }
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            StateComponent stateComponent = cg.getStateComponent(entity);
            if (stateComponent.state == EnemyState.IDLE)
                setWanderBehavior(entity);
            else if (stateComponent.state == EnemyState.HUNT)
                setPursueBehavior(entity);
            else if (stateComponent.state == EnemyState.FLEE)
                setReturnBehavior(entity);
        }
        // the states and behaviors got updated in the behavior tree
        // and are ready to be 
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
    }

    private void setReturnBehavior(Entity entity) {
        Spawn spawn = cg.getSpawn(entity);
        GameLocation spawnPosition = new GameLocation(spawn.spawnPosX, spawn.spawnPosY);

        CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance<>(
                cg.getSteering(entity),
                cg.getDetectionProximity(entity));

        Seek<Vector2> seek = new Seek<>(cg.getSteering(entity))
                .setTarget(spawnPosition);

        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(cg.getSteering(entity));
        prioritySteering.add(collisionAvoidance);
        prioritySteering.add(seek);

        cg.getSteering(entity).steeringBehavior = prioritySteering;
    }

    private void setWanderBehavior(Entity entity) {
        Spawn spawn = cg.getSpawn(entity);
        GameLocation spawnPosition = new GameLocation(spawn.spawnPosX, spawn.spawnPosY);
        Wander<Vector2> wander = new Wander<>(cg.getSteering(entity));
//                .setAlignTolerance(1);
//                .setDecelerationRadius(10);
//                .setFaceEnabled(false);
//                .setTarget(spawnPosition);
//                .setTimeToTarget(0.1f);
//                .setWanderOffset(3f);
//                .setWanderRadius(1);
//                .setWanderRate(1);
        // create component to store different steering behaviors
        Seek<Vector2> seek = new Seek<>(cg.getSteering(entity));
        if (TimeSystem.second % 2 == 0) {
            Vector2 newSpot = generateRandomPosition(entity);
            GameLocation random = new GameLocation(newSpot);
            cg.getMovementBehavior(entity).wander.setTarget(random);
        }
        cg.getSteering(entity).steeringBehavior = cg.getMovementBehavior(entity).wander;
    }

    private float randomX(Entity entity) {
        Size size = cg.getSize(entity);
        Position pos = cg.getPosition(entity);
        Rectangle spawnArea = getSpawnArea(cg.getSpawn(entity));
        if (spawnArea != null) {
            float max = (spawnArea.x + spawnArea.width) - size.width;
            return generateRandom(spawnArea.x, max);
        }
        return 0;
    }

    private float randomY(Entity entity) {
        Size size = cg.getSize(entity);
        Position pos = cg.getPosition(entity);
        Rectangle spawnArea = getSpawnArea(cg.getSpawn(entity));
        if (spawnArea != null) {
            float max = (spawnArea.y + spawnArea.height) - size.height;
            return generateRandom(spawnArea.y, max);
        }
        return 0;
    }

    private Rectangle getSpawnArea(Spawn spawn) {
        float enemySpawnX = spawn.spawnPosX;
        float enemySpawnY = spawn.spawnPosY;
        MapObjects spawns = gameMapProperties.getMapLayer(gameMapProperties.ENEMY_SPAWNS).getObjects();
        for (int i = 0; i < spawns.getCount(); i++) {
            Rectangle spawnArea = ((RectangleMapObject) spawns.get(i)).getRectangle();
            float spawnCenterX = spawnArea.x + (spawnArea.width / 2);
            float spawnCenterY = spawnArea.y + (spawnArea.height / 2);
            if (spawnCenterX == enemySpawnX && spawnCenterY == enemySpawnY)
                return spawnArea;
        }
        return null;
    }

    private float generateRandom(float min, float max) {
        // range: [min,max)
        // max is not included
        Random random = new Random();
        return random.nextInt((int) (max - min)) + min;
    }

    private Vector2 generateRandomPosition(Entity entity) {
        Position pos = cg.getPosition(entity);
        Vector2 enemyPos = new Vector2(pos.x, pos.y);
        Vector2 newSpot = new Vector2(randomX(entity), randomY(entity));
//        while (newSpot.dst(enemyPos) < 50) {
//            newSpot = new Vector2(randomX(entity), randomY(entity));
//        }
        return newSpot;
    }
}