package com.mygdx.game.ai.tasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.Enemy;

public class isPursuing extends LeafTask<MobEntity> {

    public isPursuing() {}

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        if (enemyIsPursuing(enemy))
            return Status.SUCCEEDED;
        return Status.FAILED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private boolean enemyIsPursuing(Entity entity) {
        return entity.getComponent(Enemy.class).state == Enemy.States.PURSUE;
    }
}