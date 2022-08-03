package com.mygdx.game.screens;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameMapProperties;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.systems.CollisionSystem;

public class GameRaycastCollisionDetector implements RaycastCollisionDetector<Vector2> {
    GameMapProperties gameMapProperties;

    public GameRaycastCollisionDetector(GameMapProperties gameMapProperties) {
        this.gameMapProperties = gameMapProperties;
    }

    @Override
    public boolean collides(Ray<Vector2> ray) {
        return findCollision(null ,ray);
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {
        // need to look for possible overlaps against all map objects
        // the player
        // other enemies
        // obstacles
        // loop through collision layer and check overlaps
        return thereIsCollision();
    }

    private boolean thereIsCollision() {
        MapObjects collisions = gameMapProperties.getMapLayer(GameMapProperties.COLLISIONS).getObjects();
        for (int i = 0; i < collisions.getCount(); i++) {

        }
        return false;
    }
}
