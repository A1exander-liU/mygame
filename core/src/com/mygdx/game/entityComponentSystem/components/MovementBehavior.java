package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;

public class MovementBehavior implements Component {
    public Arrive<Vector2> pursue;
    public Seek<Vector2> back;
    public Seek<Vector2> wander;

    public MovementBehavior(Entity entity) {
        Steering steering = entity.getComponent(Steering.class);
        Spawn spawn = entity.getComponent(Spawn.class);
        pursue = new Arrive<>(steering);
        back = new Seek<>(steering);
        wander = new Seek<>(steering);
        GameLocation spawnPosition = new GameLocation(spawn.spawnPosX, spawn.spawnPosY);
        wander.setTarget(spawnPosition);
    }

}
