package com.mygdx.game.engine.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
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
import com.mygdx.game.engine.components.Enemy;
import com.mygdx.game.engine.components.ID;
import com.mygdx.game.engine.components.Player;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Size;
import com.mygdx.game.engine.components.Spawn;
import com.mygdx.game.engine.components.SpawnArea;
import com.mygdx.game.engine.components.StateComponent;
import com.mygdx.game.engine.systems.TimeSystem;
import com.mygdx.game.utils.GameRaycastCollisionDetector;

import java.util.Random;
// 3 states: idle(random walk), hunting(pursue and attack player), flee(return)
public class SteeringSystem extends EntitySystem {
    ComponentGrabber cg;
    MyGame root;
    GameMapProperties gameMapProperties;
    ImmutableArray<Entity> enemies;
    ImmutableArray<Entity> entities;
    ImmutableArray<Entity> spawns;
    Entity player;
    MapObjects spawnPoints;

    public SteeringSystem(ComponentGrabber cg, MyGame root, GameMapProperties gameMapProperties) {
        super(5);
        this.cg = cg;
        this.root = root;
        this.gameMapProperties = gameMapProperties;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        entities = MyGame.engine.getEntitiesFor(Family.exclude(Player.class).get());
        spawns = MyGame.engine.getEntitiesFor(Families.spawns);
        spawnPoints = gameMapProperties.getMapLayer(GameMapProperties.ENEMY_SPAWNS).getObjects();
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
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

        cg.getSteering(entity).steeringBehavior = new Arrive<>(cg.getSteering(entity))
                .setArrivalTolerance(47)
                .setTimeToTarget(0.1f)
                .setTarget(cg.getSteering(player));
    }

    private void setReturnBehavior(Entity entity) {
        Spawn spawn = cg.getSpawn(entity);
        GameLocation spawnPosition = new GameLocation(spawn.spawnPosX, spawn.spawnPosY);

        // create new ray cast collision avoidance
        RaycastObstacleAvoidance<Vector2> avoidance = new RaycastObstacleAvoidance<>(cg.getSteering(entity));
        // creating the collision detector
        RaycastCollisionDetector<Vector2> detector = new GameRaycastCollisionDetector(gameMapProperties, entity);
        // set the ray configs
        avoidance.setRayConfiguration(
                new CentralRayWithWhiskersConfiguration<>(cg.getSteering(entity), 10f, 10f, 25f)
        );
        // setting the collision detector
        avoidance.setRaycastCollisionDetector(detector);
        // set min distance to start avoiding the collision
        avoidance.setDistanceFromBoundary(15f);

        Seek<Vector2> seek = new Seek<>(cg.getSteering(entity))
                .setTarget(spawnPosition);

        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(cg.getSteering(entity));
        prioritySteering.add(avoidance);
        prioritySteering.add(seek);

        cg.getSteering(entity).steeringBehavior = prioritySteering;
    }

    private void setWanderBehavior(Entity entity) {
        if (TimeSystem.second - cg.getMovementBehavior(entity).previousTargetUpdate >= 4) {
            Vector2 newSpot = generateRandomPosition(entity);
            GameLocation random = new GameLocation(newSpot);
            cg.getMovementBehavior(entity).wander.setTarget(random);
        }
        cg.getSteering(entity).steeringBehavior = cg.getMovementBehavior(entity).wander;

    }

    private float randomX(Entity entity) {
        Size size = cg.getSize(entity);
        Rectangle spawnArea = getSpawnArea(cg.getSpawn(entity));
        if (spawnArea != null) {
            float max = (spawnArea.x + spawnArea.width) - size.width;
            return generateRandom(spawnArea.x, max);
        }
        return 0;
    }

    private float randomY(Entity entity) {
        Size size = cg.getSize(entity);
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
        MapObjects spawns = gameMapProperties.getMapLayer(GameMapProperties.ENEMY_SPAWNS).getObjects();
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
        return new Vector2(randomX(entity), randomY(entity));
    }
}
