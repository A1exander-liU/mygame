package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameLocation;

public class Steering implements Component, Steerable<Vector2> {
    public Vector2 linearVelocity = new Vector2(75, 75);
    public float angularVelocity = 1f;
    public float boundingRadius = 50;
    public boolean tagged;
    public float zeroLinearSpeedThreshold = 0.1f;
    public float maxLinearSpeed = 75f;
    public float maxLinearAcceleration = 75f;
    public float maxAngularSpeed = 1f;
    public float maxAngularAcceleration = 1f;
    public Vector2 position = new Vector2();
    public float orientation;
    public boolean independentFacing = false;
    public SteeringBehavior<Vector2> steeringBehavior;
    private static final SteeringAcceleration<Vector2> steeringAcceleration =
            new SteeringAcceleration<>(new Vector2());
    // store reference to access
    public Entity entity;

    public Steering(Entity entity) {
        this.entity = entity;
        position.x = entity.getComponent(Position.class).x;
        position.y = entity.getComponent(Position.class).y;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    public void setBoundingRadius(float boundingRadius) {
        this.boundingRadius = boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new GameLocation();
    }

    public void update(float delta) {
        // update the position with position component of this entity
        Position pos = entity.getComponent(Position.class);
        position.x = pos.x;
        position.y = pos.y;
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringAcceleration);
            applySteering(steeringAcceleration, delta);
        }
    }

    private void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        // apply to change to position of the entity

        applyVelocityToPosition(steering, time);
        linearVelocity.mulAdd(steering.linear, time).limit(getMaxLinearSpeed());
    }

    private void applyVelocityToPosition(SteeringAcceleration<Vector2> steering, float time) {
        if (!steering.linear.isZero()) {
            Position pos = entity.getComponent(Position.class);
            // update old position to position before update
            pos.oldX = pos.x;
            pos.oldY = pos.y;
            // set position of this component to pos component of this entity
            pos.position.x = pos.x;
            pos.position.y = pos.y;
            // apply velocity
            pos.position.mulAdd(linearVelocity, time);
            // update the pos component of entity with newly calculated pos here
            pos.x = pos.position.x;
            pos.y = pos.position.y;
        }
    }
}
