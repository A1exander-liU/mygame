package com.mygdx.game.ai.tasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.game.entityComponentSystem.MobEntity;
import com.mygdx.game.entityComponentSystem.components.MovementBehavior;
import com.mygdx.game.entityComponentSystem.components.Position;
import com.mygdx.game.entityComponentSystem.components.Steering;

public class setToPursue extends LeafTask<MobEntity> {
    private Entity player;

    public setToPursue(Entity player) {
        this.player = player;
    }

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        setToPursueBehavior(enemy);
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return null;
    }

    private void setToPursueBehavior(Entity entity) {
        updatePlayerSteeringPosition();
        entity.getComponent(MovementBehavior.class).pursue
                .setTimeToTarget(0.1f)
                .setDecelerationRadius(1)
                .setArrivalTolerance(47)
                .setTarget(player.getComponent(Steering.class));
        entity.getComponent(Steering.class).steeringBehavior =
                entity.getComponent(MovementBehavior.class).pursue;
    }

    private void updatePlayerSteeringPosition() {
        Position pos = player.getComponent(Position.class);
        Steering steering = player.getComponent(Steering.class);
        steering.position.x = pos.x;
        steering.position.y = pos.y;
    }
}
