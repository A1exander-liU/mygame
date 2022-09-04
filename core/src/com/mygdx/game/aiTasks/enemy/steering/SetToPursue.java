package com.mygdx.game.aiTasks.enemy.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.utils.componentutils.Families;
import com.mygdx.game.engine.MobEntity;
import com.mygdx.game.engine.components.MovementBehavior;
import com.mygdx.game.engine.components.Position;
import com.mygdx.game.engine.components.Steering;

public class SetToPursue extends LeafTask<MobEntity> {
    private Entity player;

    public SetToPursue() {}

    @Override
    public void start() {
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
    }

    @Override
    public Status execute() {
        MobEntity enemy = getObject();
        setToPursueBehavior(enemy);
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<MobEntity> copyTo(Task<MobEntity> task) {
        return task;
    }

    private void setToPursueBehavior(Entity entity) {
        PrioritySteering<Vector2> combined = new PrioritySteering<>(entity.getComponent(Steering.class));
        updatePlayerSteeringPosition();
        entity.getComponent(MovementBehavior.class).pursue
                .setTimeToTarget(0.1f)
                .setDecelerationRadius(1)
                .setArrivalTolerance(47)
                .setTarget(player.getComponent(Steering.class));
        combined.add(entity.getComponent(MovementBehavior.class).pursue);
        combined.add(entity.getComponent(MovementBehavior.class).collisionAvoidance);
        entity.getComponent(Steering.class).steeringBehavior = combined;
    }

    private void updatePlayerSteeringPosition() {
        Position pos = player.getComponent(Position.class);
        Steering steering = player.getComponent(Steering.class);
        steering.position.x = pos.x;
        steering.position.y = pos.y;
    }
}
