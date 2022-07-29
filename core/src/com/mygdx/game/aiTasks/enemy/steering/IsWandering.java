package com.mygdx.game.aiTasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.Enemy;

public class IsWandering extends LeafTask<MobEntity> {

    public IsWandering() {}

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        if (enemyIsWandering(enemy))
            return Status.SUCCEEDED;
        return Status.FAILED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private boolean enemyIsWandering(Entity entity) {
        return entity.getComponent(Enemy.class).state == Enemy.States.WANDER;
    }
}
