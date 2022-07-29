package com.mygdx.game.aiTasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.MovementBehavior;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class SetToWander extends LeafTask<MobEntity> {

    public SetToWander() {}

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        setToWanderBehavior(enemy);
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private void setToWanderBehavior(Entity entity) {
        Steering steering = entity.getComponent(Steering.class);
        MovementBehavior movementBehavior = entity.getComponent(MovementBehavior.class);
        steering.steeringBehavior = movementBehavior.wander;
    }
}
