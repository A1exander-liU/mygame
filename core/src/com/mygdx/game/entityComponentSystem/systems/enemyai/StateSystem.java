package com.mygdx.game.entityComponentSystem.systems.enemyai;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.entityComponentSystem.ComponentGrabber;
import com.mygdx.game.entityComponentSystem.EnemyState;
import com.mygdx.game.entityComponentSystem.Families;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.EnemyStateMachine;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Spawn;
import com.mygdx.game.entityComponentSystem.components.StateComponent;


public class StateSystem extends EntitySystem {
    ComponentGrabber cg;
    GameMapProperties gameMapProperties;

    ImmutableArray<Entity> enemies;
    Entity player;

    public StateSystem(ComponentGrabber cg, GameMapProperties gameMapProperties) {
        super(5);
        this.cg = cg;
        this.gameMapProperties = gameMapProperties;
        enemies = MyGame.engine.getEntitiesFor(Families.enemies);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        setStateToIdle();
    }

    @Override
    public Engine getEngine() {
        return MyGame.engine;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            checkForEnemiesHunting(entity);
            checkForEnemiesFleeing(entity);
            checkForEnemiesIdling(entity);
        }
    }

    private void setStateToIdle() {
        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);
            EnemyStateMachine stateMachine = cg.getStateMachine(entity);
            stateMachine.setInitialState(EnemyState.IDLE);
        }
    }

    private boolean enemyAggravated(Entity entity) {
        Rectangle enemySpawn = buildSpawnArea(entity);
        Rectangle playerRect = buildEntityArea(player);
        return playerRect.overlaps(enemySpawn);
    }

    private Rectangle buildSpawnArea(Entity entity) {
        Spawn enemy = cg.getSpawn(entity);
        MapObjects spawns = gameMapProperties.getMapLayer(GameMapProperties.ENEMY_SPAWNS).getObjects();
        for (int i = 0; i < spawns.getCount(); i++) {
            Rectangle spawn = ((RectangleMapObject) spawns.get(i)).getRectangle();
            float spawnX = spawn.x + (spawn.width / 2);
            float spawnY = spawn.y + (spawn.height / 2);
            if (spawnX == enemy.spawnPosX && spawnY == enemy.spawnPosY)
                return spawn;
        }
        return null;
    }

    private Rectangle buildEntityArea(Entity entity) {
        Position pos = cg.getPosition(entity);
        Size size = cg.getSize(entity);
        return new Rectangle(pos.x, pos.y, size.width, size.height);
    }

    private boolean enemyTooFarAway(Entity entity) {
        Position pos = cg.getPosition(entity);
        Spawn spawn = cg.getSpawn(entity);
        Vector2 enemyPos = new Vector2(pos.x, pos.y);
        Vector2 spawnPos = new Vector2(spawn.spawnPosX, spawn.spawnPosY);
        return spawnPos.dst(enemyPos) >= 400;
    }

    private boolean enemyNearSpawn(Entity entity) {
        Position pos = cg.getPosition(entity);
        Spawn spawn = cg.getSpawn(entity);
        Vector2 enemyPos = new Vector2(pos.x, pos.y);
        Vector2 spawnPos = new Vector2(spawn.spawnPosX, spawn.spawnPosY);
        return spawnPos.dst(enemyPos) <= 10;
    }

    private void checkForEnemiesHunting(Entity entity) {
        EnemyStateMachine stateMachine = cg.getStateMachine(entity);
        if (stateMachine.getCurrentState() == EnemyState.IDLE) {
            if (enemyAggravated(entity))
                stateMachine.changeState(EnemyState.HUNT);
        }

    }

    private void checkForEnemiesFleeing(Entity entity) {
        EnemyStateMachine stateMachine = cg.getStateMachine(entity);
        if (stateMachine.getCurrentState() == EnemyState.HUNT) {
            if (enemyTooFarAway(entity))
                stateMachine.changeState(EnemyState.FLEE);
        }
    }

    private void checkForEnemiesIdling(Entity entity) {
        EnemyStateMachine stateMachine = cg.getStateMachine(entity);
        if (stateMachine.getCurrentState() == EnemyState.FLEE) {
            if (enemyNearSpawn(entity))
                stateMachine.changeState(EnemyState.IDLE);
        }

    }
}
