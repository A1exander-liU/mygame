package com.mygdx.game.ai.tasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.MovementBehavior;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Size;
import com.mygdx.game.entityComponentSystem.components.Spawn;
import com.mygdx.game.entityComponentSystem.components.Steering;
import com.mygdx.game.entityComponentSystem.systems.TimeSystem;

import java.util.Random;

public class ChangeTarget extends LeafTask<MobEntity> {
    private float interval;
    private GameMapProperties gameMapProperties;

    public ChangeTarget(GameMapProperties gameMapProperties) {
        this.gameMapProperties = gameMapProperties;
    }

    @Override
    public void start() {
        interval = 2;
    }

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        if (elapsedTimePassesInterval(enemy)) {
            setRandomTarget(enemy);
            return Status.SUCCEEDED;
        }
        return Status.FAILED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return null;
    }

    private void modifyInterval(float interval) {
        this.interval = interval;
    }

    private boolean elapsedTimePassesInterval(Entity entity) {
        MovementBehavior movementBehavior = entity.getComponent(MovementBehavior.class);
        return TimeSystem.time - movementBehavior.previousTargetUpdate >= interval;

    }

    private Vector2 generateRandomPosition(Entity entity) {
        Position pos = entity.getComponent(Position.class);
        return new Vector2(randomX(entity), randomY(entity));
    }

    private float randomX(Entity entity) {
        Size size = entity.getComponent(Size.class);
        Position pos = entity.getComponent(Position.class);
        Rectangle spawnArea = getSpawnArea(entity.getComponent(Spawn.class));
        if (spawnArea != null) {
            float max = (spawnArea.x + spawnArea.width) - size.width;
            return generateRandom(spawnArea.x, max);
        }
        return 0;
    }

    private float randomY(Entity entity) {
        Size size = entity.getComponent(Size.class);
        Position pos = entity.getComponent(Position.class);
        Rectangle spawnArea = getSpawnArea(entity.getComponent(Spawn.class));
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
        Random random = new Random();
        return random.nextInt((int) (max - min)) + min;
    }

    private void setRandomTarget(Entity entity) {
        MovementBehavior movementBehavior = entity.getComponent(MovementBehavior.class);
        Vector2 newPosition = generateRandomPosition(entity);
        movementBehavior.wander.setTarget(new GameLocation(newPosition));
    }
}
