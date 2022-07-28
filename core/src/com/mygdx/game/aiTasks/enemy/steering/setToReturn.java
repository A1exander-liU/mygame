package com.mygdx.game.aiTasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.DetectionProximity;
import com.mygdx.game.entityComponentSystem.components.MovementBehavior;
import com.mygdx.game.entityComponentSystem.components.Spawn;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class setToReturn extends LeafTask<MobEntity> {

    public setToReturn() {
    }

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        setToReturnBehavior(enemy);
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private void setToReturnBehavior(Entity entity) {
        Steering steering = entity.getComponent(Steering.class);
        DetectionProximity detectionProximity = entity.getComponent(DetectionProximity.class);
        Spawn spawn = entity.getComponent(Spawn.class);
        MovementBehavior movementBehavior = entity.getComponent(MovementBehavior.class);
        GameLocation spawnPosition = new GameLocation(spawn.spawnPosX, spawn.spawnPosY);
        movementBehavior.back.setTarget(spawnPosition);
        PrioritySteering<Vector2> combined = new PrioritySteering<>(steering);
        combined.add(movementBehavior.back);
        combined.add(movementBehavior.collisionAvoidance);
        entity.getComponent(Steering.class).steeringBehavior =
                combined;
    }
}
