package com.mygdx.game.screens;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;

public class GameRaycastCollisionDetector implements RaycastCollisionDetector<Vector2> {

    public GameRaycastCollisionDetector() {
        
    }

    @Override
    public boolean collides(Ray<Vector2> ray) {
        return findCollision(null ,ray);
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {

        return false;
    }
}
