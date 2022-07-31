package com.mygdx.game.aiTasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.engine.MobEntity;
import com.mygdx.game.engine.components.Enemy;

public class IsReturning extends LeafTask<MobEntity> {

    public IsReturning() {}

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        if (enemyIsReturning(enemy))
            return Status.SUCCEEDED;
        return Status.FAILED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private boolean enemyIsReturning(Entity entity) {
        return entity.getComponent(Enemy.class).state == Enemy.States.RETURN;
    }
}
