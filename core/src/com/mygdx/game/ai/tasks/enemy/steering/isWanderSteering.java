package com.mygdx.game.ai.tasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.MovementBehavior;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class isWanderSteering extends LeafTask<MobEntity> {

    public isWanderSteering() {}

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        if (enemySteeringIsWander(enemy))
            return Status.SUCCEEDED;
        return Status.FAILED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private boolean enemySteeringIsWander(Entity entity) {
        return entity.getComponent(Steering.class).steeringBehavior
                == entity.getComponent(MovementBehavior.class).wander;
    }
}