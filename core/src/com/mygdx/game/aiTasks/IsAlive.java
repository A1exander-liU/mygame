package com.mygdx.game.aiTasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.engine.MobEntity;
import com.mygdx.game.engine.components.Enemy;

public class IsAlive extends LeafTask<MobEntity> {

    public IsAlive() {}

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
