package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utils.map.GameLocation;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.Families;

import java.util.Random;

public class MovementBehavior implements Component {
    public Arrive<Vector2> pursue;
    public Seek<Vector2> back;
    public Seek<Vector2> wander;
    public CollisionAvoidance<Vector2> collisionAvoidance;
    public float previousTargetUpdate = 0;

    public MovementBehavior(Entity entity) {
        Steering steering = entity.getComponent(Steering.class);
        DetectionProximity detectionProximity = entity.getComponent(DetectionProximity.class);
        pursue = new Arrive<>(steering);
        back = new Seek<>(steering);
        wander = new Seek<>(steering);
        collisionAvoidance = new CollisionAvoidance<>(steering, detectionProximity);
        GameLocation spawnPosition = new GameLocation(generateRandomPosition(entity));
        wander.setTarget(spawnPosition);
    }

    private Vector2 generateRandomPosition(Entity entity) {
        return new Vector2(randomX(entity), randomY(entity));
    }

    private float randomX(Entity entity) {
        Size size = entity.getComponent(Size.class);
        Rectangle spawnArea = getSpawnArea(entity);
        if (spawnArea != null) {
            float max = (spawnArea.x + spawnArea.width) - size.width;
            return generateRandom(spawnArea.x, max);
        }
        return 0;
    }

    private float randomY(Entity entity) {
        Size size = entity.getComponent(Size.class);
        Rectangle spawnArea = getSpawnArea(entity);
        if (spawnArea != null) {
            float max = (spawnArea.y + spawnArea.height) - size.height;
            return generateRandom(spawnArea.y, max);
        }
        return 0;
    }

    private Rectangle getSpawnArea(Entity entity) {
        ImmutableArray<Entity> spawns = MyGame.engine.getEntitiesFor(Families.spawns);
        for (int i = 0; i < spawns.size(); i++) {
            Entity spawn = spawns.get(i);
            SpawnArea spawnArea = spawn.getComponent(SpawnArea.class);
            Position pos = spawn.getComponent(Position.class);
            Size size = spawn.getComponent(Size.class);
            if (spawnArea.owner == entity) {
                return new Rectangle(pos.x, pos.y, size.width, size.height);
            }
        }
        return null;
    }

    private float generateRandom(float min, float max) {
        Random random = new Random();
        return random.nextInt((int) (max - min)) + min;
    }

}
