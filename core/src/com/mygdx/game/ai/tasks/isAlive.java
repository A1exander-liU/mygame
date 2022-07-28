package com.mygdx.game.ai.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.Enemy;

public class isAlive extends LeafTask<MobEntity> {
    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        if (enemyIsAlive(enemy))
            return Status.SUCCEEDED;
        return Status.FAILED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private boolean enemyIsAlive(Entity entity) {
        return entity.getComponent(Enemy.class).isAlive;
    }
}