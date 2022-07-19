package com.mygdx.game.entityComponentSystem.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;

public class Steering implements Component, Steerable<Vector2> {
    public Vector2 linearVelocity = new Vector2(5, 5);
    public float angularVelocity = 5f;
    public float boundingRadius;
    public boolean tagged;
    public float zeroLinearSpeedThreshold = 0.1f;
    public float maxLinearSpeed = 5f;
    public float maxLinearAcceleration;
    public float maxAngularSpeed;
    public float maxAngularAcceleration;
    public Vector2 position;
    public float orientation;
    public boolean independentFacing = false;
    public SteeringBehavior<Vector2> steeringBehavior;
    public static final SteeringAcceleration<Vector2> steeringAcceleration =
            new SteeringAcceleration<>(new Vector2());
    // store reference to access
    public Entity entity;

    public Steering(Entity entity) {
        this.entity = entity;
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
        return 0;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return null;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    public void update(float delta) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringAcceleration);
            applySteering(delta);
        }
    }

    private void applySteering(float time) {
        this.position.mulAdd(linearVelocity, time);
        this.linearVelocity.mulAdd(steeringAcceleration.linear, time).limit(this.getMaxLinearSpeed());
        if (!independentFacing) {
            
        }
    }
}
