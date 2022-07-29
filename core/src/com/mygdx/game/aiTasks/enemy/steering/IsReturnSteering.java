package com.mygdx.game.aiTasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.MovementBehavior;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class IsReturnSteering extends LeafTask<MobEntity> {

    public IsReturnSteering() {}

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        if (enemySteeringIsReturn(enemy))
            return Status.SUCCEEDED;
        return Status.FAILED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private boolean enemySteeringIsReturn(Entity entity) {
        return entity.getComponent(Steering.class).steeringBehavior
                == entity.getComponent(MovementBehavior.class).back;
    }
}
