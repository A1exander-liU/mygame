package com.mygdx.game.utils.map;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class GameLocation implements Location<Vector2> {
    Vector2 position;
    float orientation;

    public GameLocation() {
        this.position = new Vector2();
        this.orientation = 0;
    }

    public GameLocation(Vector2 position) {
        this.position = position;
    }

    public GameLocation(float x, float y) {
        position = new Vector2(x, y);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
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
}
